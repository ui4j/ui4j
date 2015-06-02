package com.ui4j.webkit.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebEngine;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import com.ui4j.api.util.Ui4jException;
import com.ui4j.spi.Ui4jExecutionTimeoutException;
import com.ui4j.webkit.dom.WebKitDocument;

public class WebKitProxy {

    public static class CallableExecutor implements Runnable {

        private CountDownLatch latch;

        private Callable<Object> callable;

        private Object result;

        public CallableExecutor(CountDownLatch latch, Callable<Object> callable) {
            this.latch = latch;
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                result = callable.call();
            } catch (Exception e) {
                throw new Ui4jException(e);
            } finally {
                latch.countDown();
            }
        }

        public Object getResult() {
            return result;
        }
    }

	private static class LoadListener implements ChangeListener<Boolean> {

		private CountDownLatch latch;

		public LoadListener(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if (newValue.equals(Boolean.FALSE)) { // finished loading
				latch.countDown();
			}
		}
	};

    private static class LoadingRunner implements Runnable {

    	private WebEngine engine;

    	private CountDownLatch latch;

    	private boolean loading;

    	public LoadingRunner(WebEngine engine, CountDownLatch latch) {
    		this.engine = engine;
    		this.latch = latch;
    	}

		@Override
		public void run() {
			loading = engine.getLoadWorker().isRunning();
			latch.countDown();
		}

		public boolean isLoading() {
			return loading;
		}
    }

    public static class WebKitInterceptor {

    	@RuntimeType
        public static Object execute(@SuperCall Callable<Object> callable,
        										@This Object that,
        										@Origin Method method,
												@AllArguments Object[] arguments) {

            if (that instanceof WebKitDocument) {
            	WebKitDocument document = (WebKitDocument) that;
            	boolean loading = false;
            	if (Platform.isFxApplicationThread()) {
            		loading = document.getEngine().getLoadWorker().isRunning();
            	} else {
            		CountDownLatch loadingLatch = new CountDownLatch(1);
            		LoadingRunner loadingRunner = new LoadingRunner(document.getEngine(), loadingLatch);
            		Platform.runLater(loadingRunner);
            		try {
						loadingLatch.await(60, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						throw new Ui4jExecutionTimeoutException(e, 60, TimeUnit.SECONDS);
					}
            		loading = loadingRunner.isLoading();
            		if (loading) {
            			CountDownLatch listenerLatch = new CountDownLatch(1);
            			LoadListener listener = new LoadListener(listenerLatch);
            			Platform.runLater(() -> document.getEngine().getLoadWorker().runningProperty().addListener(listener));
                		try {
    						listenerLatch.await(60, TimeUnit.SECONDS);
    					} catch (InterruptedException e) {
    						throw new Ui4jExecutionTimeoutException(e, 60, TimeUnit.SECONDS);
    					}
            			Platform.runLater(() -> document.getEngine().getLoadWorker().runningProperty().removeListener(listener));
            			document.refreshDocument();
            		}
            	}
            }

            Object ret = null;
            if (!Platform.isFxApplicationThread()) {
                CountDownLatch latch = new CountDownLatch(1);
                CallableExecutor executor = new CallableExecutor(latch, callable);
                Platform.runLater(executor);
                try {
                    latch.await(60, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new Ui4jExecutionTimeoutException(e, 60, TimeUnit.SECONDS);
                }
                ret = executor.getResult();
            } else {
                try {
                    ret = callable.call();
                } catch (Exception e) {
                    throw new Ui4jException(e);
                }
            }
            return ret;
        }
    }

	private Constructor<?> constructor;

	private Class<?> proxyClass;

	public WebKitProxy(Class<?> klass, Class<?>[] constructorArguments) {
    	Class<?> loaded = new ByteBuddy()
								.subclass(klass)
								.method(ElementMatchers.any()
												.and(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class))
												.and(ElementMatchers.not(ElementMatchers.nameStartsWith("getEngine")))))
						    	.intercept(MethodDelegation.to(WebKitInterceptor.class))
						    	.make()
						    	.load(WebKitProxy.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
						    	.getLoaded();
    	this.proxyClass = loaded;
    	try {
			constructor = loaded.getConstructor(constructorArguments);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new Ui4jException(e);
		}
	}

    public Object newInstance(Object[] arguments) {
    	Object instance = null;
    	try {
			instance = constructor.newInstance(arguments);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new Ui4jException(e);
		}
    	return instance;
    }

    public Class<?> getProxyClass() {
    	return proxyClass;
    }
}
