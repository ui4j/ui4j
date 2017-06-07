package com.ui4j.webkit.browser;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.w3c.dom.Node;
import com.sun.webkit.dom.DocumentImpl;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.browser.SelectorType;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
import com.ui4j.api.dom.Window;
import com.ui4j.api.event.DocumentListener;
import com.ui4j.api.event.DocumentLoadEvent;
import com.ui4j.api.util.Logger;
import com.ui4j.api.util.LoggerFactory;
import com.ui4j.spi.EventManager;
import com.ui4j.spi.EventRegistrar;
import com.ui4j.spi.JavaScriptEngine;
import com.ui4j.spi.NativeEventManager;
import com.ui4j.spi.PageContext;
import com.ui4j.webkit.dom.WebKitDocument;
import com.ui4j.webkit.dom.WebKitElement;
import com.ui4j.webkit.dom.WebKitPage;
import com.ui4j.webkit.dom.WebKitWindow;
import com.ui4j.webkit.spi.SizzleSelectorEngine;
import com.ui4j.webkit.spi.W3CEventRegistrar;
import com.ui4j.webkit.spi.W3CSelectorEngine;
import com.ui4j.webkit.spi.WebKitJavaScriptEngine;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebKitPageContext implements PageContext {

    private Logger log = LoggerFactory.getLogger(getClass());

    private EventRegistrar eventRegistrar;

    private EventManager eventManager = new NativeEventManager(this);

    private SelectorEngine selector;

    private PageConfiguration configuration;

    private Map<DocumentImpl, Document> contentDocuments = new WeakHashMap<>();

    private Map<DocumentImpl, SelectorEngine> selectorEngines = new WeakHashMap<>();

    public static class DefaultErrorEventHandler implements EventHandler<WebErrorEvent> {

        private Logger log = LoggerFactory.getLogger(getClass());

        @Override
        public void handle(WebErrorEvent event) {
            log.error("Javascript error: " + event.getMessage());
        }
    }

    public static class ExceptionListener implements ChangeListener<Throwable> {

        private Logger log;

        public ExceptionListener(Logger log) {
            this.log = log;
        }

        @Override
        public void changed(ObservableValue<? extends Throwable> observable,
                Throwable oldValue, Throwable newValue) {
            log.error(newValue.getMessage());
        }
    };

    public static class GlobalEventCleaner implements DocumentListener {

        @Override
        public void onLoad(DocumentLoadEvent event) {
            Document document = event.getDocument();
            if (document == null) {
                return;
            }
            List<Element> elements = document.queryAll("[ui4j-registered-event=true]");
            if (elements.isEmpty()) {
                return;
            }
            for (Element next : elements) {
                next.unbind();
                next.removeAttribute("ui4j-registered-event");
                next.removeProperty("events");
            }
        }
    }

    public WebKitPageContext(PageConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public EventRegistrar getEventRegistrar() {
        return eventRegistrar;
    }

    public Element createElement(Object node, Document document, JavaScriptEngine engine) {
        return (Element) new WebKitElement((Node) node, document, this, (WebKitJavaScriptEngine) engine);
    }

    public Document createDocument(JavaScriptEngine engine) {
        DocumentImpl documentImpl = (DocumentImpl) ((WebKitJavaScriptEngine) engine).getEngine().getDocument();

        WebEngine webEngine = (WebEngine) engine.getEngine();
        if (configuration.getUserAgent() != null) {
            webEngine.setUserAgent(configuration.getUserAgent());
        }
        webEngine.getLoadWorker().exceptionProperty().addListener(new ExceptionListener(log));
        webEngine.setOnError(new DefaultErrorEventHandler());
        Document document = new WebKitDocument(this, documentImpl, (WebKitJavaScriptEngine) engine);
        selector = initializeSelectorEngine(document, (WebKitJavaScriptEngine) engine);
        return document;
    }

    public Document getContentDocument(DocumentImpl documentImpl, JavaScriptEngine engine) {
        synchronized (this) {
            Document existingDocument = contentDocuments.get(documentImpl);
            if (existingDocument != null) {
                return existingDocument;
            } else {
                Document document = new WebKitDocument(this, documentImpl, (WebKitJavaScriptEngine) engine);
                contentDocuments.put(documentImpl, document);

                SelectorEngine selector = initializeSelectorEngine(document, (WebKitJavaScriptEngine) engine);
                selectorEngines.put(documentImpl, selector);

                return document;
            }
        }
    }

    public void onLoad(Document document) {
        this.eventRegistrar = new W3CEventRegistrar(this);
    }

    public Window createWindow(Document document) {
        return (Window) new WebKitWindow(document);
    }

    public WebKitPage newPage(Object view, JavaScriptEngine engine, Window window, Stage stage, Scene scene, Document document, int pageId) {
        WebView webView = (WebView) view;
        WebKitPage page = new WebKitPage(webView, (WebKitJavaScriptEngine) engine, window, stage, scene, document, pageId);
        page.addDocumentListener(new GlobalEventCleaner());
        return page;
    }

    @Override
    public SelectorEngine getSelectorEngine() {
        return selector;
    }

    public SelectorEngine getSelectorEngine(org.w3c.dom.Document documentImpl) {
        SelectorEngine contentDocumentSelectorEngine = selectorEngines.get(documentImpl);
        if (contentDocumentSelectorEngine == null) {
            return selector;
        }
        return contentDocumentSelectorEngine;
    }

    protected SelectorEngine initializeSelectorEngine(Document document, WebKitJavaScriptEngine engine) {
        SelectorEngine selector = null;
        if (configuration.getSelectorEngine().equals(SelectorType.SIZZLE)) {
            String sizzle = readSizzle();
            boolean foundSizzle = Boolean.parseBoolean(engine.getEngine().executeScript("typeof window.Sizzle === 'function'").toString());
            if (!foundSizzle) {
                engine.getEngine().executeScript(sizzle);
            }
            selector = new SizzleSelectorEngine(this, document, engine);
        } else {
            selector = new W3CSelectorEngine(this, document, engine);
        }
        return selector;
    }

    protected String readSizzle() {
        return readFromClasspath("/com/ui4j/webkit/sizzle.js");
    }

    protected String readFromClasspath(String path) {
        try(java.util.Scanner scanner = new java.util.Scanner(getClass().getResourceAsStream(path))) {
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        }
    }

    public PageConfiguration getConfiguration() {
        return configuration;
    }
}
