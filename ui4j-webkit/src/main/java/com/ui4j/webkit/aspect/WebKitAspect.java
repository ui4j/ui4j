package com.ui4j.webkit.aspect;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.ui4j.api.util.Ui4jException;
import com.ui4j.spi.Ui4jExecutionTimeoutException;
import com.ui4j.webkit.dom.WebKitDocument;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebEngine;

@Aspect
public class WebKitAspect {

    public static class CallableExecutor implements Runnable {

        private CountDownLatch latch;

        private ProceedingJoinPoint callable;

        private Object result;

        public CallableExecutor(CountDownLatch latch, ProceedingJoinPoint callable) {
            this.latch = latch;
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                result = callable.proceed();
            } catch (Throwable e) {
                throw new Ui4jException(e);
            } finally {
                latch.countDown();
            }
        }

        public Object getResult() {
            return result;
        }
    }

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

    @Around("within(com.ui4j.webkit.dom..*) && execution(* *(..))")
    public Object intercept(ProceedingJoinPoint jp) throws Throwable {

        Object that = jp.getTarget();
        Method method = ((MethodSignature) jp.getSignature()).getMethod();

        if (method.getName().equals("getEngine")) {
            return jp.proceed();
        }

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
            CallableExecutor executor = new CallableExecutor(latch, jp);
            Platform.runLater(executor);
            try {
                latch.await(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new Ui4jExecutionTimeoutException(e, 60, TimeUnit.SECONDS);
            }
            ret = executor.getResult();
        } else {
            try {
                ret = jp.proceed();
            } catch (Exception e) {
                throw new Ui4jException(e);
            }
        }
        return ret;
    }
}
