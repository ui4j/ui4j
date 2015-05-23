package com.ui4j.jxbrowser;

import java.util.ArrayList;
import java.util.List;

import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.dom.Element;
import com.ui4j.jxbrowser.js.JsDocument;
import com.ui4j.jxbrowser.js.JsElement;
import com.ui4j.jxbrowser.js.JsNodeList;

public class JxW3CSelectorEngine implements SelectorEngine {

	private JsDocument document;

	public JxW3CSelectorEngine(JsDocument document) {
		this.document = document;
	}

	@Override
	public Element query(String selector) {
		JsElement jsElement = document.querySelector(selector);
		JxElement jxElement = new JxElement(jsElement);
		return jxElement;
	}

	@Override
	public List<Element> queryAll(String selector) {
		JsNodeList nodeList = document.querySelectorAll(selector);
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
		JxElement jxElement = (JxElement) element;
		JsElement jsElement = jxElement.getJsElement();
		JsElement jsFoundElement = jsElement.querySelector(selector);
		JxElement jxFoundElement = new JxElement(jsFoundElement);
		return jxFoundElement;
	}

	@Override
	public List<Element> queryAll(Element element, String selector) {
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
}
