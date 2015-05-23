package com.ui4j.jxbrowser;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSObject;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
import com.ui4j.api.event.EventHandler;
import com.ui4j.api.util.Ui4jException;
import com.ui4j.jxbrowser.js.JsDocument;
import com.ui4j.jxbrowser.proxy.JsProxy;

public class JxDocument implements Document {

	private SelectorEngine selectorEngine;

	private ThreadLocalRandom random = ThreadLocalRandom.current();

	private DOMDocument document;

	private DOMElement body;

	private Browser browser;

	private JsDocument jsDocument;

	public JxDocument(Browser browser, SelectorEngine selectorEngine) {
		this.browser = browser;
		document = browser.getDocument();
		body = document.getDocumentElement().findElement(By.tagName("body"));
		this.selectorEngine = selectorEngine;
		JSObject jsObjectDocument = (JSObject) browser.executeJavaScriptAndReturnValue("document");
		jsDocument = JsProxy.to(jsObjectDocument, JsDocument.class);
	}

	@Override
	public Element query(String selector) {
		waitIfLoading();
		return selectorEngine.query(selector);
	}

	@Override
	public List<Element> queryAll(String selector) {
		waitIfLoading();
		return selectorEngine.queryAll(selector);
	}

	@Override
	public Element createElement(String tagName) {
		waitIfLoading();
		DOMElement element = document.createElement(tagName);
		String id = String.valueOf(Math.abs(random.nextInt()));
		element.setAttribute("ui4j-id", id);
		body.appendChild(element);
		Element jxElement = selectorEngine.query("[ui4j-id='" + id + "']");
		element.removeAttribute("ui4j-id");
		body.removeChild(element);
		return jxElement;
	}

	@Override
	public void bind(String event, EventHandler handler) {
		waitIfLoading();
	}

	@Override
	public void unbind(String event) {
		waitIfLoading();
	}

	@Override
	public void unbind() {
		waitIfLoading();
	}

	@Override
	public Element getBody() {
		waitIfLoading();
		return selectorEngine.query("body");
	}

	@Override
	public void setTitle(String title) {
		waitIfLoading();
		jsDocument.setTitle(title);
	}

	@Override
	public String getTitle() {
		waitIfLoading();
		return jsDocument.getTitle();
	}

	@Override
	public List<Element> parseHTML(String html) {
		waitIfLoading();
		return null;
	}

	@Override
	public void trigger(String eventType, Element element) {		
		waitIfLoading();
	}

	@Override
	public Element getElementFromPoint(int x, int y) {
		waitIfLoading();
		return null;
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
