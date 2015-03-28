package com.ui4j.jxbrowser;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.teamdev.jxbrowser.chromium.JSFunction;
import com.teamdev.jxbrowser.chromium.JSObject;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.ui4j.api.dom.CheckBox;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
import com.ui4j.api.dom.Form;
import com.ui4j.api.dom.Input;
import com.ui4j.api.dom.Node;
import com.ui4j.api.dom.Option;
import com.ui4j.api.dom.RadioButton;
import com.ui4j.api.dom.Select;
import com.ui4j.api.event.EventHandler;
import com.ui4j.api.util.Point;
import com.ui4j.api.util.Ui4jException;

public class JxElement implements Element {

	private JSObject element;

	public JxElement(JSObject obj) {
		this.element = obj;
	}

	@Override
	public List<Element> getChildren() {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean isEqualNode(Node node) {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean isSameNode(Node node) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Document getDocument() {
		throw new MethodNotSupportedException();
	}

	@Override
	public void removeProperty(String key) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Object getProperty(String key) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void setProperty(String key, Object value) {
		throw new MethodNotSupportedException();
	}

	@Override
	public String getAttribute(String name) {
		return (String) invoke("getAttribute", name);
	}

	@Override
	public Element setAttribute(String name, String value) {
		invoke("setAttribute", name, value);
		return this;
	}

	@Override
	public Element setAttribute(Map<String, String> attributes) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element removeAttribute(String name) {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean hasAttribute(String name) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element addClass(String... names) {
		String className = (String) getValue("className");
		if (names != null) {
			StringBuilder builder = new StringBuilder();
			for (String name : names) {
				builder.append(name);
				builder.append(" ");
			}
			if (className.isEmpty()) {
				className = builder.toString().trim();
			} else {
				className = className + " " + builder.toString().trim();
			}
		}
		element.set("className", JSObject.create(className));
		return this;
	}

	@Override
	public Element removeClass(String... names) {
		String className = (String) getValue("className");
		List<String> classes = new ArrayList<String>(asList(className.split(" ")));
		classes.removeAll(asList(names));
		StringBuilder builder = new StringBuilder();
		for (String name : classes) {
			builder.append(name);
			builder.append(" ");
		}
		element.set("className", JSObject.create(builder.toString().trim()));
		return null;
	}

	@Override
	public boolean hasClass(String name) {
		String className = (String) getValue("className");
		if (className != null) {
			return asList(className.split(" ")).contains(name);
		}
		return false;
	}

	@Override
	public Element toggleClass(String name) {
		if (hasClass(name)) {
			removeClass(name);
		} else {
			addClass(name);
		}
		return this;
	}

	@Override
	public List<String> getClasses() {
		String className = (String) getValue("className");
		if (className != null) {
			return asList(className.split(" "));
		}
		return Collections.emptyList();
	}

	@Override
	public String getText() {
		return (String) getValue("textContent");
	}

	@Override
	public String getTagName() {
		return ((String) getValue("tagName")).toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String getValue() {
		return (String) getValue("value");
	}

	@Override
	public Element setValue(String value) {
		element.set("value", JSObject.create(value));
		return this;
	}

	@Override
	public Element bind(String event, EventHandler handler) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element bindClick(EventHandler handler) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element setTitle(String title) {
		setValue("title", title);
		return this;
	}

	@Override
	public String getTitle() {
		return (String) getValue("title");
	}

	@Override
	public Element unbind(EventHandler handler) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element unbind(String event) {
		throw new MethodNotSupportedException();
	}

	@Override
	public List<Element> find(String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element unbind() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element empty() {
		throw new MethodNotSupportedException();
	}

	@Override
	public void remove() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element click() {
		invoke("click");
		return this;
	}

	@Override
	public Element getParent() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Input getInput() {
		throw new MethodNotSupportedException();
	}

	@Override
	public CheckBox getCheckBox() {
		throw new MethodNotSupportedException();
	}

	@Override
	public RadioButton getRadioButton() {
		return null;
	}

	@Override
	public Option getOption() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Form getForm() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Select getSelect() {
		throw new MethodNotSupportedException();
	}

	@Override
	public String getId() {
		return (String) getValue("id");
	}

	@Override
	public Element setId(String id) {
		setValue("id", id);
		return this;
	}

	@Override
	public Element append(String html) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element append(Element element) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element prepend(Element element) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element after(String html) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element after(Element element) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element before(String html) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element before(Element element) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element prepend(String html) {
		throw new MethodNotSupportedException();
	}

	@Override
	public String getInnerHTML() {
		return (String) getValue("innerHTML");
	}

	@Override
	public String getOuterHTML() {
		return (String) getValue("outerHTML");
	}

	@Override
	public Element setInnerHTML(String html) {
		element.set("innerHTML", JSObject.create(html));
		return this;
	}

	@Override
	public boolean isHtmlElement() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element setText(String text) {
		element.set("textContent", JSObject.create(text));
		return this;
	}

	@Override
	public Element setTabIndex(int index) {
		element.set("tabIndex", JSObject.create(index));
		return this;
	}

	@Override
	public int getTabIndex() {
		return ((Double) getValue("tabIndex")).intValue();
	}

	@Override
	public Element focus() {
		invoke("focus");
		return this;
	}

	@Override
	public Element query(String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public List<Element> queryAll(String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element on(String event, String selector, EventHandler handler) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element off() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element off(String event) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element off(String event, String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Point getOffset() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Point getPosition() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element detach() {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean isAttached() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element scrollIntoView(boolean alignWithTop) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element setCss(String propertyName, String value) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element setCss(Map<String, String> properties) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element removeCss(String propertyName) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element setCss(String propertyName, String value, String important) {
		throw new MethodNotSupportedException();
	}

	@Override
	public String getCss(String propertyName) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element getPrev() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element getNext() {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean hasChildNodes() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element appendTo(Element parent) {
		throw new MethodNotSupportedException();
	}

	@Override
	public float getOuterHeight() {
		throw new MethodNotSupportedException();
	}

	@Override
	public float getClientHeight() {
		return ((Double) getValue("clientHeight")).floatValue();
	}

	@Override
	public float getClientWidth() {
		return ((Double) getValue("clientWidth")).floatValue();
	}

	@Override
	public float getOuterWidth() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element hide() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element show() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element cloneElement() {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean contains(Element element) {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean is(String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element getOffsetParent() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element replaceWidth(String html) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element replaceWidth(Element element) {
		throw new MethodNotSupportedException();
	}

	@Override
	public List<Element> getSiblings(String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public List<Element> getSiblings() {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean isEmpty() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element getNextSibling() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element closest(String selector) {
		throw new MethodNotSupportedException();
	}

	protected void setValue(String name, Object value) {
		JSValue jsValue = null;
		if (value instanceof Boolean) {
			jsValue = JSObject.create((Boolean) value);
		} if (value instanceof Number) {
			jsValue = JSObject.create((long) value);
		} else {
			jsValue = JSObject.create(String.valueOf(value));
		}
		element.set(name, jsValue);
	}

	protected Object getValue(String name) {
		JSValue jsValue = element.get(name);

		if (jsValue.isNull() || jsValue.isUndefined()) {
			return null;
		} else if (jsValue.isBoolean()) {
			return jsValue.getBoolean();
		} else if (jsValue.isNumber()) {
			return jsValue.getNumber();
		} else if (jsValue.isString()) {
			return jsValue.getString();
		} else if (jsValue.isTrue()) {
			return true;
		} else if (jsValue.isFalse()) {
			return false;
		} else if (jsValue.isObject()) {
			return (JSObject) jsValue;
		} else {
			return null;
		}
	}

	protected Object invoke(String functionName, Object... arguments) {
		JSValue value = element.get(functionName);

		if (value.isNull() || value.isUndefined()) {
			throw new Ui4jException("Undefined function: " + functionName);
		}

		if (!value.isFunction()) {
			throw new Ui4jException(functionName + " is not function");
		}

		JSFunction func = (JSFunction) value;
		List<JSValue> args = new ArrayList<>();
		for (Object next : arguments) {
			if (next == null) {
				args.add(JSObject.createNull());
			}
			if (next instanceof Boolean) {
				args.add(JSObject.create((Boolean) next));
			} if (next instanceof Number) {
				args.add(JSObject.create((long) next));
			} else {
				args.add(JSObject.create(String.valueOf(next)));
			}
		}

		JSValue ret = null;
		if (args.isEmpty()) {
			ret = func.invokeAndReturnValue(element);
		} else {
			ret = func.invokeAndReturnValue(element, args.toArray(new JSValue[args.size()]));
		}

		if (ret.isNull() || ret.isUndefined()) {
			return null;
		} else if (ret.isBoolean()) {
			return ret.getBoolean();
		} else if (ret.isNumber()) {
			return ret.getNumber();
		} else if (ret.isString()) {
			return ret.getString();
		} else if (ret.isTrue()) {
			return true;
		} else if (ret.isFalse()) {
			return false;
		} else if (ret.isObject()) {
			return (JSObject) ret;
		} else {
			return null;
		}
	}
}
