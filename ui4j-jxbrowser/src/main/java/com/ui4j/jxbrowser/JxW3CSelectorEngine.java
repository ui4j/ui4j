package com.ui4j.jxbrowser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSObject;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.dom.Element;
import com.ui4j.api.util.Ui4jException;
import com.ui4j.jxbrowser.js.JsDocument;
import com.ui4j.jxbrowser.js.JsElement;
import com.ui4j.jxbrowser.js.JsNodeList;
import com.ui4j.jxbrowser.proxy.JsProxy;
import com.ui4j.jxbrowser.proxy.JxEmptyElementProxy;

public class JxW3CSelectorEngine implements SelectorEngine {

    private Browser browser;

    private JxEmptyElementProxy emptyElementProxy = new JxEmptyElementProxy();

    private JsDocument jsDocument;

    public JxW3CSelectorEngine(Browser browser) {
        this.browser = browser;
        JSObject jsObjectDocument = (JSObject) browser.executeJavaScriptAndReturnValue("document");
        jsDocument = JsProxy.to(jsObjectDocument, JsDocument.class);
    }

    @Override
    public Element query(String selector) {
        waitIfLoading();
        JsElement jsElement = jsDocument.querySelector(selector);
        if (jsElement == null) {
            return emptyElementProxy.getEmptyElement();
        }
        JxElement jxElement = new JxElement(jsElement);
        return jxElement;
    }

    @Override
    public List<Element> queryAll(String selector) {
        waitIfLoading();
        JsNodeList nodeList = jsDocument.querySelectorAll(selector);
        int length = nodeList.getLength();
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            JsElement jsElement = nodeList.getItem(i);
            JxElement jxElement = new JxElement(jsElement);
            elements.add(jxElement);
        }
        return elements;
    }

    @Override
    public Element query(Element element, String selector) {
        waitIfLoading();
        JxElement jxElement = (JxElement) element;
        JsElement jsElement = jxElement.getJsElement();
        JsElement jsFoundElement = jsElement.querySelector(selector);
        if (jsFoundElement == null) {
            return emptyElementProxy.getEmptyElement();
        }
        JxElement jxFoundElement = new JxElement(jsFoundElement);
        return jxFoundElement;
    }

    @Override
    public List<Element> queryAll(Element element, String selector) {
        waitIfLoading();
        JxElement jxElement = (JxElement) element;
        JsElement jsElement = jxElement.getJsElement();
        JsNodeList nodeList = jsElement.querySelectorAll(selector);
        int length = nodeList.getLength();
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            JsElement nextJsElement = nodeList.getItem(i);
            JxElement nextJxElement = new JxElement(nextJsElement);
            elements.add(nextJxElement);
        }
        return elements;
    }

    protected void waitIfLoading() {
        if (browser.isLoading()) {
            CountDownLatch latch = new CountDownLatch(1);
            JxDocumentLoadWaitAdapter adapter = new JxDocumentLoadWaitAdapter(latch);
            browser.addLoadListener(adapter);
            try {
                latch.await(60, TimeUnit.SECONDS);
                browser = adapter.getBrowser();
            } catch (InterruptedException e) {
                throw new Ui4jException(e);
            }
            JSObject jsObjectDocument = (JSObject) browser.executeJavaScriptAndReturnValue("document");
            jsDocument = JsProxy.to(jsObjectDocument, JsDocument.class);
        }
    }
}
