package com.ui4j.jxbrowser;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
import com.ui4j.api.event.EventHandler;
import com.ui4j.jxbrowser.js.JsDocument;

public class JxDocument implements Document {

	private SelectorEngine selectorEngine;

	private ThreadLocalRandom random = ThreadLocalRandom.current();

	private DOMDocument document;

	private DOMElement body;

	private JsDocument jsDocument;

	public JxDocument(Browser browser, SelectorEngine selectorEngine, JsDocument jsDocument) {
		document = browser.getDocument();
		body = document.getDocumentElement().findElement(By.tagName("body"));
		this.selectorEngine = selectorEngine;
		this.jsDocument = jsDocument;
	}

	@Override
	public Element query(String selector) {
		return selectorEngine.query(selector);
	}

	@Override
	public List<Element> queryAll(String selector) {
		return selectorEngine.queryAll(selector);
	}

	@Override
	public Element createElement(String tagName) {
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
	}

	@Override
	public void unbind(String event) {
	}

	@Override
	public void unbind() {
	}

	@Override
	public Element getBody() {
		return selectorEngine.query("body");
	}

	@Override
	public void setTitle(String title) {
		jsDocument.setTitle(title);
	}

	@Override
	public String getTitle() {
		return jsDocument.getTitle();
	}

	@Override
	public List<Element> parseHTML(String html) {
		return null;
	}

	@Override
	public void trigger(String eventType, Element element) {		
	}

	@Override
	public Element getElementFromPoint(int x, int y) {
		return null;
	}
}
