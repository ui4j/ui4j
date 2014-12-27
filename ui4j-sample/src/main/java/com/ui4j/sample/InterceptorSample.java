package com.ui4j.sample;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import sun.net.www.protocol.http.Handler;

import com.ui4j.api.browser.BrowserFactory;

@SuppressWarnings("restriction")
public class InterceptorSample {

    public static interface ConnectionInterceptor {

        URLConnection intercept(URL url, URLConnection conn);
    }

    public static class CustomURLHandler extends URLStreamHandler {

        private Handler httpHandler = new Handler();

		private sun.net.www.protocol.https.Handler httpsHandler = new sun.net.www.protocol.https.Handler();

        private Method httpOpenConnection;

        private Method httpsOpenConnection;

        private ConnectionInterceptor interceptor;

        public CustomURLHandler(ConnectionInterceptor interceptor) {
            this.interceptor = interceptor;

            try {
                httpOpenConnection = httpHandler.getClass().getDeclaredMethod("openConnection", URL.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
            if (!httpOpenConnection.isAccessible()) {
                httpOpenConnection.setAccessible(true);
            }

            try {
                httpsOpenConnection = httpsHandler.getClass().getDeclaredMethod("openConnection", URL.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
            if (!httpsOpenConnection.isAccessible()) {
                httpsOpenConnection.setAccessible(true);
            }
        }

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            URLConnection conn = null;
            if ("http".equals(u.getProtocol())) {
                try {
                    conn = (URLConnection) httpOpenConnection.invoke(httpHandler, u);
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            if ("https".equals(u.getProtocol())) {
                try {
                    conn = (URLConnection) httpsOpenConnection.invoke(httpsHandler, u);
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            return interceptor.intercept(u, conn);
        }
    }

    public static class CustomURLStreamHandlerFactory implements URLStreamHandlerFactory {

        private ConnectionInterceptor interceptor;

        public CustomURLStreamHandlerFactory(ConnectionInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        @Override
        public URLStreamHandler createURLStreamHandler(String protocol) {
            if (protocol != null && protocol.startsWith("http")) {
                return new CustomURLHandler(interceptor);
            }
            return null;
        }
    }

    public static void main(String[] args) throws MalformedURLException {

        ConnectionInterceptor interceptor = new ConnectionInterceptor() {

            @Override
            public URLConnection intercept(URL url, URLConnection conn) {
                conn.setRequestProperty("Custom-Header", "foobar");
                return conn;
            }
        };

        URL.setURLStreamHandlerFactory(new CustomURLStreamHandlerFactory(interceptor));

        BrowserFactory
                .getWebKit()
                .navigate("http://httpbin.org/headers")
                .show();
    }
}
