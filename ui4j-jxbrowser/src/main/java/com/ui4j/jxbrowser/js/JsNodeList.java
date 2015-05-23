package com.ui4j.jxbrowser.js;

public interface JsNodeList {

	@JsProperty("length")
	int getLength();

	@JsFunction("item")
	JsElement getItem(int index);
}
