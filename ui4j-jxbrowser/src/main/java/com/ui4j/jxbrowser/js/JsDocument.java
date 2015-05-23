package com.ui4j.jxbrowser.js;

public interface JsDocument extends JsObject {

	@JsFunction("querySelector")
	JsElement querySelector(String selector);

	@JsFunction("querySelectorAll")
	JsNodeList querySelectorAll(String selector);

	@JsProperty("title")
	String getTitle();

	@JsProperty("title")
	void setTitle(String title);
}
