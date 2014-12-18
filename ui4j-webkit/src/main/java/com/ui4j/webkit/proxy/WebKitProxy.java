package com.ui4j.webkit.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassLoadingStrategy;
import net.bytebuddy.instrumentation.MethodDelegation;
import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.AllArguments;
import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.Origin;
import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.RuntimeType;
import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.SuperCall;
import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import com.ui4j.api.dom.Element;
import com.ui4j.api.util.Ui4jException;
import com.ui4j.spi.Ui4jExecutionTimeoutException;

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

    public static class WebKitInterceptor {

    	private static Element emptyElement;

    	static {
    		emptyElement = new WebKitEmptyElementProxy().getEmptyElement();
    	}

    	@RuntimeType
        public static Object execute(@SuperCall Callable<Object> callable,
        										@This Object that,
        										@Origin Method method,
												@AllArguments Object[] arguments) {
            Parameter[] parameters = method.getParameters();
            if (parameters.length == 1 &&
            			arguments.length == 1 &&
            			Element.class.isAssignableFrom(method.getReturnType()) &&
            			Element.class.isAssignableFrom(parameters[0].getType()) &&
            			((Element) arguments[0]).isEmpty()) {
            	return that;
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
            Class<?> retType = method.getReturnType();
            if (ret == null && Element.class.isAssignableFrom(retType)) {
            	return emptyElement;
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
												.and(ElementMatchers.not(ElementMatchers.nameStartsWith("wait")))))
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
