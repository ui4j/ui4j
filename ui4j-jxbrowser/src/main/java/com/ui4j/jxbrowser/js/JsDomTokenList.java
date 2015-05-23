package com.ui4j.jxbrowser.js;

public interface JsDomTokenList {

	@JsFunction("add")
	void add(String name);

	@JsFunction("remove")
	void remove(String name);

	@JsFunction("toggle")
	void toggle(String name);

	@JsFunction("contains")
	boolean contains(String name);

	@JsProperty("length")
	int getLength();

	@JsFunction("item")
	String getItem(int index);
}
