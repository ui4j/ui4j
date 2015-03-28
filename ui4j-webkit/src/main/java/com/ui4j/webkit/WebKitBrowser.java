package com.ui4j.webkit;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import org.w3c.dom.Node;

import com.sun.webkit.dom.DocumentImpl;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Window;
import com.ui4j.api.event.DocumentListener;
import com.ui4j.api.event.DocumentLoadEvent;
import com.ui4j.api.util.Logger;
import com.ui4j.api.util.LoggerFactory;
import com.ui4j.spi.PageContext;
import com.ui4j.spi.ShutdownListener;
import com.ui4j.spi.Ui4jExecutionTimeoutException;
import com.ui4j.webkit.browser.WebKitPage;
import com.ui4j.webkit.browser.WebKitPageContext;
import com.ui4j.webkit.browser.WebKitWindow;
import com.ui4j.webkit.dom.WebKitDocument;
import com.ui4j.webkit.dom.WebKitElement;
import com.ui4j.webkit.proxy.WebKitProxy;
import com.ui4j.webkit.spi.WebKitJavaScriptEngine;

class WebKitBrowser implements BrowserEngine {

    private static CountDownLatch startupLatch = new CountDownLatch(1);

    private static AtomicBoolean launchedJFX = new AtomicBoolean(false);

    private ShutdownListener shutdownListener;

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
								                Window.class, Document.class
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
    
    	private boolean headless;

    	public LauncherThread(boolean headless) {
    		this.headless = headless;
		}

        @Override
        public void run() {
        	new ApplicationLauncher().launch(ApplicationImpl.class, headless);
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

        public WorkerLoadListener(WebKitJavaScriptEngine engine, PageContext context, DocumentListener documentListener) {
            this.engine = engine;
            this.configuration = (WebKitPageContext) context;
            this.documentListener = documentListener;
        }

        @Override
        public void changed(ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) {
            if (newState == Worker.State.SUCCEEDED) {
                Document document = configuration.createDocument(engine);
                configuration.onLoad(document);
                Window window = configuration.createWindow(document);
                DocumentLoadEvent event = new DocumentLoadEvent(window);
                documentListener.onLoad(event);
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

        public WebViewCreator(String url,
                                PageContext context, DocumentListener listener, PageConfiguration configuration) {
            this(url, context, listener, null, configuration);
        }

        public WebViewCreator(String url,
                PageContext context, DocumentListener listener, CountDownLatch latch, PageConfiguration configuration) {
            this.url = url;
            this.latch = latch;
            this.context = context;
            this.listener = listener;
            this.configuration = configuration;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            webView = new WebView();
            engine = new WebKitJavaScriptEngine(webView.getEngine());
            if (configuration.getUserAgent() != null) {
            	engine.getEngine().setUserAgent(configuration.getUserAgent());
            }
            engine.getEngine().load(url);
            WorkerLoadListener loadListener = new WorkerLoadListener(engine, context, listener);
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
    }

    @Override
    public synchronized Page navigate(String url) {
        return navigate(url, new PageConfiguration());
    }

    @Override
    public Page navigate(String url, PageConfiguration configuration) {
        WebKitPageContext context = new WebKitPageContext(configuration,
                                elementFactory, documentFactory,
                                windowFactory, pageFactory);
        CountDownLatch documentReadyLatch = new CountDownLatch(1);
        SyncDocumentListener adapter = new SyncDocumentListener(documentReadyLatch);
        WebViewCreator creator = null;
        if (Platform.isFxApplicationThread()) {
            creator = new WebViewCreator(url, context, adapter, configuration);
            creator.run();
        } else {
            CountDownLatch webViewLatch = new CountDownLatch(1);
            creator = new WebViewCreator(url, context, adapter, webViewLatch, configuration);
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
        Page page = ((WebKitPageContext) context).newPage(webView, creator.getEngine(), adapter.getWindow(), adapter.getDocument());
        return page;
    }

    public synchronized void start() {
        if (launchedJFX.compareAndSet(false, true) &&
                            !Platform.isFxApplicationThread()) {
        	boolean headless = System.getProperty("ui4j.headless") != null ? true : false;
            new LauncherThread(headless).start();
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
}
