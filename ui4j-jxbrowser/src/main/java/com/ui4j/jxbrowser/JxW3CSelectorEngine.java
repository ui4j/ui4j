package com.ui4j.jxbrowser;

import java.util.List;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSFunction;
import com.teamdev.jxbrowser.chromium.JSObject;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.dom.Element;

public class JxW3CSelectorEngine implements SelectorEngine {

	private Browser browser;

	public JxW3CSelectorEngine(Browser browser) {
		this.browser = browser;
	}

	@Override
	public Element query(String selector) {
		JSObject document = (JSObject) browser.executeJavaScriptAndReturnValue("document");
		JSFunction querySelector = (JSFunction) document.get("querySelector");
		JSValue value = querySelector.invokeAndReturnValue(document, JSObject.create(selector));

		if (value.isNull() || value.isUndefined() || !value.isObject()) {
			return null;
		}

		JSObject obj = (JSObject) value;
		JxElement element = new JxElement(obj);
		return element;
	}

	@Override
	public List<Element> queryAll(String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element query(Element element, String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public List<Element> queryAll(Element element, String selector) {
		throw new MethodNotSupportedException();
	}
}
