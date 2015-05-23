package com.ui4j.jxbrowser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.ui4j.jxbrowser.js.JsDomTokenList;
import com.ui4j.jxbrowser.js.JsElement;
import com.ui4j.jxbrowser.js.JsNodeList;

public class JxElement implements Element {

	private JsElement element;

	public JxElement(JsElement element) {
		this.element = element;
	}

	@Override
	public List<Element> getChildren() {
		List<Element> elements = new ArrayList<>();
		JsNodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			JsElement next = nodes.getItem(i);
			elements.add(new JxElement(next));
		}
		return elements;
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
		return element.getAttribute(name);
	}

	@Override
	public Element setAttribute(String name, String value) {
		element.setAttribute(name, value);
		return this;
	}

	@Override
	public Element setAttribute(Map<String, String> attributes) {
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			setAttribute(entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Override
	public Element removeAttribute(String name) {
		element.removeAttribute(name);
		return this;
	}

	@Override
	public boolean hasAttribute(String name) {
		return element.hasAttribute(name);
	}

	@Override
	public Element addClass(String... names) {
		JsDomTokenList list = element.getClassList();
		for (String name : names) {
			list.add(name);
		}
		return this;
	}

	@Override
	public Element removeClass(String... names) {
		JsDomTokenList list = element.getClassList();
		for (String name : names) {
			list.remove(name);
		}
		return this;
	}

	@Override
	public boolean hasClass(String name) {
		return element.getClassList().contains(name);
	}

	@Override
	public Element toggleClass(String name) {
		element.getClassList().toggle(name);
		return this;
	}

	@Override
	public List<String> getClasses() {
		JsDomTokenList list = element.getClassList();
		int length = list.getLength();
		List<String> classes = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			String name = list.getItem(i);
			classes.add(name);
		}
		return classes;
	}

	@Override
	public String getText() {
		return element.getTextContent();
	}

	@Override
	public String getTagName() {
		return element.geTagName().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String getValue() {
		return element.getValue();
	}

	@Override
	public Element setValue(String value) {
		element.setValue(value);
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
		element.setTitle(title);
		return this;
	}

	@Override
	public String getTitle() {
		return element.getTitle();
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
        List<Element> elements = new ArrayList<>();
        return elements;
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
		JsElement parent = element.getParentNode();
		parent.removeChild(this.element.getJSObject());
	}

	@Override
	public Element click() {
		element.click();
		return this;
	}

	@Override
	public Element getParent() {
		return new JxElement(element.getParentNode());
	}

	@Override
	public Input getInput() {
		return new Input(this);
	}

	@Override
	public CheckBox getCheckBox() {
		return new CheckBox(this);
	}

	@Override
	public RadioButton getRadioButton() {
		return new RadioButton(this);
	}

	@Override
	public Option getOption() {
		return new Option(this);
	}

	@Override
	public Form getForm() {
		return new Form(this);
	}

	@Override
	public Select getSelect() {
		return new Select(this);
	}

	@Override
	public String getId() {
		return element.getId();
	}

	@Override
	public Element setId(String id) {
		element.setId(id);
		return this;
	}

	@Override
	public Element append(String html) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element append(Element element) {
		JxElement jxElement = (JxElement) element;
		this.element.appendChild(jxElement.element.getJSObject());
		return this;
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
		return element.getInnerHTML();
	}

	@Override
	public String getOuterHTML() {
		return element.getOuterHTML();
	}

	@Override
	public Element setInnerHTML(String html) {
		element.setInnerHTML(html);
		return this;
	}

	@Override
	public boolean isHtmlElement() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element setText(String text) {
		element.setText(text);
		return this;
	}

	@Override
	public Element setTabIndex(int index) {
		element.setTabIndex(index);
		return this;
	}

	@Override
	public int getTabIndex() {
		return element.getTabIndex();
	}

	@Override
	public Element focus() {
		element.focus();
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
		/*invoke("scrollIntoView", alignWithTop);*/
		return this;
	}

	@Override
	public Element setCss(String propertyName, String value) {
		element.setStyle(propertyName, value);
		return this;
	}

	@Override
	public Element setCss(Map<String, String> properties) {
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			setCss(entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Override
	public Element removeCss(String propertyName) {
		setCss(propertyName, "");
		return this;
	}

	@Override
	public Element setCss(String propertyName, String value, String important) {
		throw new MethodNotSupportedException();
	}

	@Override
	public String getCss(String propertyName) {
		return element.getStyle(propertyName);
	}

	@Override
	public Element getPrev() {
		return new JxElement(element.getPreviousElementSibling());
	}

	@Override
	public Element getNext() {
		return new JxElement(element.getNextElementSibling());
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
		return element.getClientHeight();
	}

	@Override
	public float getClientWidth() {
		return element.getClientWidth();
	}

	@Override
	public float getOuterWidth() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element hide() {
        setCss("display", "none");
        return this;
	}

	@Override
	public Element show() {
        setCss("display", "");
        return this;
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
		/*return (boolean) invoke("matches", selector);*/
		return false;
	}

	@Override
	public Element getOffsetParent() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element replaceWith(String html) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element replaceWith(Element element) {
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
		return false;
	}

	@Override
	public Element getNextSibling() {
		throw new MethodNotSupportedException();
	}

	@Override
	public Element closest(String selector) {
		throw new MethodNotSupportedException();
	}

	@Override
	public Document getContentDocument() {
		throw new MethodNotSupportedException();
	}

	public JsElement getJsElement() {
		return element;
	}
}
