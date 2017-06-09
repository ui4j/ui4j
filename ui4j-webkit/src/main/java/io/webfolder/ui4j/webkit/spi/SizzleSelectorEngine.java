package io.webfolder.ui4j.webkit.spi;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import netscape.javascript.JSObject;

import org.w3c.dom.Node;

import com.sun.webkit.dom.HTMLElementImpl;

import io.webfolder.ui4j.api.browser.SelectorEngine;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Element;
import io.webfolder.ui4j.spi.PageContext;
import io.webfolder.ui4j.webkit.browser.WebKitPageContext;
import io.webfolder.ui4j.webkit.dom.WebKitElement;

public class SizzleSelectorEngine implements SelectorEngine {

    private WebKitJavaScriptEngine engine;

    private Document document;

    private PageContext context;

    public SizzleSelectorEngine(PageContext context, Document document, WebKitJavaScriptEngine engine) {
        this.context = context;
        this.document = document;
        this.engine = engine;
    }

    @Override
    public Optional<Element> query(String selector) {
        String escapedSelector = selector.replace('\'', '"');
        JSObject result = (JSObject) engine.getEngine().executeScript(format("Sizzle('%s')", escapedSelector));
        int length = (int) result.getMember("length");
        if (length <= 0) {
            return null;
        } else {
            Node found = (Node) result.getSlot(0);
            Element element = ((WebKitPageContext) context).createElement(found, document, engine);
            if (element == null) {
                return Optional.empty();
            } else {
                return Optional.of(element);
            }
        }
    }

    @Override
    public List<Element> queryAll(String selector) {
        String escapedSelector = selector.replace('\'', '"');
        JSObject result = (JSObject) engine.getEngine().executeScript(format("Sizzle('%s')", escapedSelector));
        int length = (int) result.getMember("length");
        if (length <= 0) {
            return Collections.emptyList();
        } else {
            List<Element> elements = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Node found = (Node) result.getSlot(i);
                Element element = ((WebKitPageContext) context).createElement(found, document, engine);
                elements.add(element);
            }
            return elements;
        }
    }

    @Override
    public Optional<Element> query(Element element, String selector) {
        if (!(element instanceof WebKitElement)) {
            return null;
        }
        String escapedSelector = selector.replace('\'', '"');
        WebKitElement fxElementImpl = (WebKitElement) element;
        HTMLElementImpl elementImpl = fxElementImpl.getHtmlElement();
        JSObject result = (JSObject) elementImpl.eval("Sizzle('" + escapedSelector + "', this)");
        int length = (int) result.getMember("length");
        if (length <= 0) {
            return null;
        } else {
            Node found = (Node) result.getSlot(0);
            Element ret = ((WebKitPageContext) context).createElement(found, document, engine);
            if (ret == null) {
                return Optional.empty();
            } else {
                return Optional.of(ret);
            }
        }
    }

    @Override
    public List<Element> queryAll(Element element, String selector) {
        if (!(element instanceof WebKitElement)) {
            return Collections.emptyList();
        }
        String escapedSelector = selector.replace('\'', '"');
        WebKitElement fxElementImpl = (WebKitElement) element;
        HTMLElementImpl elementImpl = fxElementImpl.getHtmlElement();
        JSObject result = (JSObject) elementImpl.eval("Sizzle('" + escapedSelector + "', this)");
        int length = (int) result.getMember("length");
        if (length <= 0) {
            return Collections.emptyList();
        } else {
            List<Element> elements = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Node found = (Node) result.getSlot(i);
                Element ret = ((WebKitPageContext) context).createElement(found, document, engine);
                elements.add(ret);
            }
            return elements;
        }
    }
}
