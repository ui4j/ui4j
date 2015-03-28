package com.ui4j.jxbrowser;

import java.util.List;

import com.teamdev.jxbrowser.chromium.Browser;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
import com.ui4j.api.event.EventHandler;

public class JxDocument implements Document {

	private Browser browser;

	private SelectorEngine selectorEngine;

	public JxDocument(Browser browser, SelectorEngine selectorEngine) {
		this.browser = browser;
		this.selectorEngine = selectorEngine;
	}

	@Override
	public Element query(String selector) {
		return selectorEngine.query(selector);
	}

	@Override
	public List<Element> queryAll(String selector) {
		return null;
	}

	@Override
	public Element createElement(String tagName) {
		return null;
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
		return null;
	}

	@Override
	public void setTitle(String title) {
	}

	@Override
	public String getTitle() {
		return null;
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
