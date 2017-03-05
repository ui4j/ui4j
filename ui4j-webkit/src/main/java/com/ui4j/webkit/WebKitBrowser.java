package com.ui4j.webkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.CookieHandler;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import org.w3c.dom.Node;

import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.network.CookieManager;
import com.sun.webkit.network.URLs;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Window;
import com.ui4j.api.event.DocumentListener;
import com.ui4j.api.event.DocumentLoadEvent;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Response;
import com.ui4j.api.util.Logger;
import com.ui4j.api.util.LoggerFactory;
import com.ui4j.api.util.Ui4jException;
import com.ui4j.spi.PageContext;
import com.ui4j.spi.ShutdownListener;
import com.ui4j.spi.Ui4jExecutionTimeoutException;
import com.ui4j.webkit.browser.WebKitPage;
import com.ui4j.webkit.browser.WebKitPage.AlertDelegationHandler;
import com.ui4j.webkit.browser.WebKitPage.ConfirmDelegationHandler;
import com.ui4j.webkit.browser.WebKitPage.PromptDelegationHandler;
import com.ui4j.webkit.browser.WebKitPageContext;
import com.ui4j.webkit.browser.WebKitURLHandler;
import com.ui4j.webkit.browser.WebKitWindow;
import com.ui4j.webkit.dom.WebKitDocument;
import com.ui4j.webkit.dom.WebKitElement;
import com.ui4j.webkit.proxy.WebKitProxy;
import com.ui4j.webkit.spi.WebKitJavaScriptEngine;

class WebKitBrowser implements BrowserEngine {

    private static CountDownLatch startupLatch = new CountDownLatch(1);

    private static AtomicBoolean launchedJFX = new AtomicBoolean(false);

    private ShutdownListener shutdownListener;

    private AtomicInteger pageCounter = new AtomicInteger(0);

    private static final Logger LOG = LoggerFactory.getLogger(WebKitBrowser.class);

    private WebKitProxy elementFactory = new WebKitProxy(WebKitElement.class, new Class[] {
                                                Node.class, Document.class,
                                                PageContext.class, WebKitJavaScriptEngine.class
    });

    private WebKitProxy documentFactory = new WebKitProxy(WebKitDocument.class, new Class[] {
                                                PageContext.class, DocumentImpl.class,
                                                WebKitJavaScriptEngine.class
    });
    
    private WebKitProxy windowFactory = new WebKitProxy(WebKitWindow.class, new Class[] {
                                                Document.class
    });
    
    private WebKitProxy pageFactory = new WebKitProxy(WebKitPage.class, new Class[] {
                                                WebView.class, WebKitJavaScriptEngine.class,
                                                Window.class, Stage.class, Scene.class,
                                                Document.class, int.class
    });

    WebKitBrowser(ShutdownListener shutdownListener) {
        this.shutdownListener = shutdownListener;
        if (!Platform.isFxApplicationThread()) {
            start();
        }
    }

    public static class ApplicationImpl extends Application {
        
        @Override
        public void start(Stage stage) {
            startupLatch.countDown();
        }
    }

    public static class SyncDocumentListener implements DocumentListener {

        private CountDownLatch latch;

        private Window window;

        private Document document;

        public SyncDocumentListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onLoad(DocumentLoadEvent event) {
            this.window = event.getWindow();
            this.document = event.getDocument();
            latch.countDown();
        }

        public Document getDocument() {
            return document;
        }

        public Window getWindow() {
            return window;
        }
    }

    public static class LauncherThread extends Thread {
    
        @Override
        public void run() {
            new ApplicationLauncher().launch(ApplicationImpl.class);
        }
    }

    public static class ExitRunner implements Runnable {

        private CountDownLatch latch;

        public ExitRunner(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            if (Platform.isFxApplicationThread()) {
                launchedJFX.set(false);
                Platform.exit();
            }
            latch.countDown();
        }        
    }

    @SuppressWarnings("rawtypes")
    public static class EmptyObservableValue implements ObservableValue {
        @Override
        public void addListener(InvalidationListener listener) { }

        @Override
        public void removeListener(InvalidationListener listener) { }

        @Override
        public void addListener(ChangeListener listener) { }

        @Override
        public Object getValue() { return null; }

        @Override
        public void removeListener(ChangeListener listener) { }
    }

    public static class WorkerLoadListener implements ChangeListener<Worker.State> {

        private WebKitPageContext configuration;

        private DocumentListener documentListener;

        private WebKitJavaScriptEngine engine;

        private WebKitURLHandler handler;

        public WorkerLoadListener(WebKitJavaScriptEngine engine, PageContext context, DocumentListener documentListener, WebKitURLHandler handler) {
            this.engine = engine;
            this.configuration = (WebKitPageContext) context;
            this.documentListener = documentListener;
            this.handler = handler;
        }

        @Override
        public void changed(ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) {
            if (newState == Worker.State.SUCCEEDED) {
                Document document = configuration.createDocument(engine);
                configuration.onLoad(document);
                Window window = configuration.createWindow(document);
                DocumentLoadEvent event = new DocumentLoadEvent(window);
                documentListener.onLoad(event);

                if (configuration.getConfiguration().getInterceptor() != null && handler != null) {
                    URLConnection connection = handler.getConnection();
                    if (handler.getConnection() != null) {
                        Map<String, List<String>> headers = connection.getHeaderFields();
                        Response response = new Response(window.getLocation(), Collections.unmodifiableMap(new HashMap<>(headers)));
                        configuration.getConfiguration().getInterceptor().afterLoad(response);
                    }
                }
            }
        }
    }
 
    public static class ProgressListener implements ChangeListener<Number> {

        private WebEngine engine;

        public ProgressListener(WebEngine engine) {
            this.engine = engine;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double progress = Math.floor((double) newValue * 100);
            if (progress % 5 == 0 || progress % 10 == 0) {
                WebKitBrowser.LOG.info(String.format("Loading %s [%d%%]", engine.getLocation(), (int) progress));
            }
        }
    }

    public static class WebViewCreator implements Runnable {

        private WebView webView;

        private String url;

        private CountDownLatch latch;

        private PageContext context;

        private DocumentListener listener;

        private WebKitJavaScriptEngine engine;

        private PageConfiguration configuration;

        private WebKitURLHandler handler;

        private Stage stage;

        private Scene scene;

        public WebViewCreator(String url,
                                PageContext context, DocumentListener listener, PageConfiguration configuration, WebKitURLHandler handler) {
            this(url, context, listener, null, configuration, handler);
        }

        public WebViewCreator(String url,
                PageContext context, DocumentListener listener, CountDownLatch latch, PageConfiguration configuration, WebKitURLHandler handler) {
            this.url = url;
            this.latch = latch;
            this.context = context;
            this.listener = listener;
            this.configuration = configuration;
            this.handler = handler;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            webView = new WebView();

            stage = new Stage();
            scene = new Scene(webView, 600, 600);
            stage.setScene(scene);

            engine = new WebKitJavaScriptEngine(webView.getEngine());
            if (configuration.getUserAgent() != null) {
                engine.getEngine().setUserAgent(configuration.getUserAgent());
            }
            if (configuration.getAlertHandler() != null) {
                engine.getEngine().setOnAlert(new AlertDelegationHandler(configuration.getAlertHandler()));
            }
            if (configuration.getPromptHandler() != null) {
                engine.getEngine().setPromptHandler(new PromptDelegationHandler(configuration.getPromptHandler()));
            }
            if (configuration.getConfirmHandler() != null) {
                engine.getEngine().setConfirmHandler(new ConfirmDelegationHandler(configuration.getConfirmHandler()));
            }
            engine.getEngine().load(url);
            WorkerLoadListener loadListener = new WorkerLoadListener(engine, context, listener, handler);
            webView.getEngine().getLoadWorker(). progressProperty().addListener(new ProgressListener(webView.getEngine()));
            // load blank pages immediately
            if (url == null || url.trim().equals("about:blank") || url.trim().equals("")) {
                loadListener.changed(new EmptyObservableValue(), Worker.State.SCHEDULED, Worker.State.SUCCEEDED);
            } else {
                engine.getEngine().getLoadWorker().stateProperty().addListener(loadListener);
            }
            installErrorHandler();

            if (latch != null) {
                latch.countDown();
            }
        }

        protected void installErrorHandler() {
            JSObject objWindow = (JSObject) engine.getEngine().executeScript("window");
            objWindow.setMember("Ui4jErrorHandler", new WebKitErrorHandler());
            engine.getEngine().executeScript("window.onerror = function(message, url, lineNumber) { Ui4jErrorHandler.onError(message, url, lineNumber); return false; }");
        }

        public WebView getWebView() {
            return webView;
        }

        public WebKitJavaScriptEngine getEngine() {
            return engine;
        }

        public Stage getStage() {
            return stage;
        }

        public Scene getScene() {
            return scene;
        }
    }

    @Override
    public synchronized Page navigate(String url) {
        return navigate(url, new PageConfiguration());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page navigate(String url, PageConfiguration configuration) {
        WebKitPageContext context = new WebKitPageContext(configuration,
                                elementFactory, documentFactory,
                                windowFactory, pageFactory);

        int pageId = pageCounter.incrementAndGet();

        Interceptor interceptor = configuration.getInterceptor();
        String ui4jUrl = url;
        WebKitURLHandler handler = null;
        if (interceptor != null) {
            String ui4jProtocol = "ui4j-" + pageId;
            ui4jUrl = ui4jProtocol + ":" + url;
            handler = new WebKitURLHandler(interceptor, configuration.isInterceptAllRequests());
            try {
                // HACK #26
                Field handlerMap = URLs.class.getDeclaredField("handlerMap");
                handlerMap.setAccessible(true);
                Map<String, URLStreamHandler> handlers = (Map<String, URLStreamHandler>) handlerMap.get(null);
                handlers.put(ui4jProtocol, handler);
                // HACK #26
            } catch (IllegalArgumentException | IllegalAccessException
                    | NoSuchFieldException | SecurityException e) {
                throw new Ui4jException(e);
            }
        }

        CountDownLatch documentReadyLatch = new CountDownLatch(1);
        SyncDocumentListener adapter = new SyncDocumentListener(documentReadyLatch);
        WebViewCreator creator = null;
        if (Platform.isFxApplicationThread()) {
            creator = new WebViewCreator(ui4jUrl, context, adapter, configuration, handler);
            creator.run();
        } else {
            CountDownLatch webViewLatch = new CountDownLatch(1);
            creator = new WebViewCreator(ui4jUrl, context, adapter, webViewLatch, configuration, handler);
            Platform.runLater(creator);
            try {
                webViewLatch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new Ui4jExecutionTimeoutException(e, 10, TimeUnit.SECONDS);
            }
        }
        try {
            documentReadyLatch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new Ui4jExecutionTimeoutException(e, 60, TimeUnit.SECONDS);
        }

        WebView webView = creator.getWebView();
        Stage stage = creator.getStage();
        Scene scene = creator.getScene();
        WebKitPage page = ((WebKitPageContext) context).newPage(webView, creator.getEngine(), adapter.getWindow(),
                stage, scene,
                adapter.getDocument(), pageId);

        return page;
    }

    public synchronized void start() {
        if (launchedJFX.compareAndSet(false, true) &&
                            !Platform.isFxApplicationThread()) {
            applyURLsHack();
            new LauncherThread().start();
            try {
                startupLatch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new Ui4jExecutionTimeoutException(e, 10, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public synchronized void shutdown() {
        if (launchedJFX.get()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(new ExitRunner(latch));
            shutdownListener.onShutdown(this);
            try {
                latch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new Ui4jExecutionTimeoutException(e, 10, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public BrowserType getBrowserType() {
        return BrowserType.WebKit;
    }

    // Hack #26
    //
    // https://github.com/ui4j/ui4j/issues/26
    //
    // WebView api doesnt let to intercept HTTP request.
    // we need to apply our modifiable handlers hack until public api supports interceptors.
    // 
    // We register custom URLStreamHandler per web page.
    // Each page has its own handler so that we could intercept the request.
    // @see Ui4jHandler class for implementation details.
    //
    // Hack #26
    private void applyURLsHack() {
        try {
            ConcurrentHashMap<Object, Object> handlers = new ConcurrentHashMap<>();
            handlers.put("about", new com.sun.webkit.network.about.Handler());
            handlers.put("data", new com.sun.webkit.network.data.Handler());
            setFinalStatic(URLs.class.getDeclaredField("handlerMap"), handlers);
        } catch (NoSuchFieldException | SecurityException |
                                IllegalArgumentException e) {
            throw new Ui4jException(e);
        }
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
            throw new Ui4jException(e);
        }
     }

    @Override
    @SuppressWarnings("rawtypes")
    public synchronized void clearCookies() {
        CookieHandler cookieHandler = CookieHandler.getDefault();
        if (cookieHandler == null) {
            return;
        }
        if (cookieHandler instanceof CookieManager) {
            CookieManager manager = (CookieManager) cookieHandler;
            Field fieldStore;
            try {
                fieldStore = manager.getClass().getDeclaredField("store");
                fieldStore.setAccessible(true);
                Object store = fieldStore.get(manager);
                Field fieldBuckets = store.getClass().getDeclaredField("buckets");
                fieldBuckets.setAccessible(true);
                Map buckets = (Map) fieldBuckets.get(store);
                buckets.clear();
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new Ui4jException(e);
            }
        } else if (cookieHandler instanceof WebKitIsolatedCookieHandler) {
            ((WebKitIsolatedCookieHandler) cookieHandler).clear();
        }
    }
}
