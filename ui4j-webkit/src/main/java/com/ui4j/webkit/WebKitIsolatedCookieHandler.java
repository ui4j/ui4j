package com.ui4j.webkit;

import static java.util.Collections.synchronizedMap;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.CookieHandler;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.scene.web.WebView;

import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.network.CookieManager;

public class WebKitIsolatedCookieHandler extends CookieHandler {

    private static final Map<Long, WebView> threadWebViewMappings = 
                                                        synchronizedMap(new WeakHashMap<>());

    private static final Map<WebView, CookieManager> cookieManagers =
                                                        synchronizedMap(new WeakHashMap<>());

    static {
        Class<?> klassNetworkContext = null;
        Class<?> klassUrlLoaderFactory = null;

        try {
            klassNetworkContext = WebKitIsolatedCookieHandler.class.getClassLoader().loadClass("com.sun.webkit.network.NetworkContext");
            klassUrlLoaderFactory = WebKitIsolatedCookieHandler.class.getClassLoader().loadClass("com.sun.webkit.network.NetworkContext$URLLoaderThreadFactory");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Constructor<?> constructor;
        
        ThreadFactory urlThreadFactory = null;

        try {
            constructor = klassUrlLoaderFactory.getDeclaredConstructor();
            constructor.setAccessible(true);
            urlThreadFactory = (ThreadFactory) constructor.newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException
                                | IllegalAccessException | IllegalArgumentException
                                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        try {
            Field fieldPool = klassNetworkContext.getDeclaredField("threadPool");
            fieldPool.setAccessible(true);

            Field keepAlive = klassNetworkContext.getDeclaredField("THREAD_POOL_KEEP_ALIVE_TIME");
            keepAlive.setAccessible(true);

            Field poolSize = klassNetworkContext.getDeclaredField("THREAD_POOL_SIZE");
            poolSize.setAccessible(true);

            int THREAD_POOL_SIZE = poolSize.getInt(null);
            long THREAD_POOL_KEEP_ALIVE_TIME = keepAlive.getLong(null);

            WebKitThreadPoolExecutor threadPool = new WebKitThreadPoolExecutor(
                    THREAD_POOL_SIZE,
                    THREAD_POOL_SIZE,
                    THREAD_POOL_KEEP_ALIVE_TIME,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    urlThreadFactory);
            threadPool.allowCoreThreadTimeOut(true);
            setFinalStatic(fieldPool, threadPool);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static class WebKitThreadPoolExecutor extends ThreadPoolExecutor {

        public WebKitThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                long keepAliveTime, TimeUnit unit,
                BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                    threadFactory);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            WebPage webPage = null;
            try {
                Field fieldCallable = r.getClass().getDeclaredField("callable");
                fieldCallable.setAccessible(true);
                Object adapter = fieldCallable.get(r);
                Field fieldTask = adapter.getClass().getDeclaredField("task");
                fieldTask.setAccessible(true);
                Object urlLoader = fieldTask.get(adapter);
                Field fieldWebPage = urlLoader.getClass().getDeclaredField("webPage");
                fieldWebPage.setAccessible(true);
                webPage = (WebPage) fieldWebPage.get(urlLoader);
            } catch (NoSuchFieldException | SecurityException |
                                        IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            try {
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
            @SuppressWarnings("unchecked")
            WebPageClient<WebView> client = webPage.getPageClient();
            WebView webView = client.getContainer();
            threadWebViewMappings.put(t.getId(), webView);
        }
    }

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        long id = Thread.currentThread().getId();
        WebView webView = threadWebViewMappings.get(id);
        CookieManager cookieManager = cookieManagers.get(webView);
        if (cookieManager == null) {
            cookieManager = new CookieManager();
            cookieManagers.put(webView, cookieManager);
        }
        return cookieManager.get(uri, requestHeaders);
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        long id = Thread.currentThread().getId();
        WebView webView = threadWebViewMappings.get(id);
        CookieManager cookieManager = cookieManagers.get(webView);
        if (cookieManager == null) {
            cookieManager = new CookieManager();
            cookieManagers.put(webView, cookieManager);
        }
        cookieManager.put(uri, responseHeaders);
    }

    private static void setFinalStatic(Field field, Object newValue) {
        try {
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, newValue);
        } catch (NoSuchFieldException | SecurityException |
                                IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
     }

    public void clear() {
        threadWebViewMappings.clear();
        cookieManagers.clear();
    }

    public void remove(WebView webView) {
        cookieManagers.remove(webView);
        List<Long> list = new ArrayList<>();
        for (Map.Entry<Long, WebView> entry : threadWebViewMappings.entrySet()) {
            if (entry.getValue().equals(webView)) {
                list.add(entry.getKey());
            }
        }
        list.forEach(id -> threadWebViewMappings.remove(id));
    }
}
