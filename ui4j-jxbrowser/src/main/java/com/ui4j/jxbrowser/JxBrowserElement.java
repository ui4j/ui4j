package com.ui4j.jxbrowser;

import java.util.List;
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

public class JxBrowserElement implements Element {

	@Override
	public List<Element> getChildren() {
		return null;
	}

	@Override
	public boolean isEqualNode(Node node) {
		return false;
	}

	@Override
	public boolean isSameNode(Node node) {
		return false;
	}

	@Override
	public Document getDocument() {
		return null;
	}

	@Override
	public void removeProperty(String key) {
	}

	@Override
	public Object getProperty(String key) {
		return null;
	}

	@Override
	public void setProperty(String key, Object value) {
	}

	@Override
	public String getAttribute(String name) {
		return null;
	}

	@Override
	public Element setAttribute(String name, String value) {
		return null;
	}

	@Override
	public Element setAttribute(Map<String, String> attributes) {
		return null;
	}

	@Override
	public Element removeAttribute(String name) {
		return null;
	}

	@Override
	public boolean hasAttribute(String name) {
		return false;
	}

	@Override
	public Element addClass(String... names) {
		return null;
	}

	@Override
	public Element removeClass(String... names) {
		return null;
	}

	@Override
	public boolean hasClass(String name) {
		return false;
	}

	@Override
	public Element toggleClass(String name) {
		return null;
	}

	@Override
	public List<String> getClasses() {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public String getTagName() {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public Element setValue(String value) {
		return null;
	}

	@Override
	public Element bind(String event, EventHandler handler) {
		return null;
	}

	@Override
	public Element bindClick(EventHandler handler) {
		return null;
	}

	@Override
	public Element setTitle(String title) {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public Element unbind(EventHandler handler) {
		return null;
	}

	@Override
	public Element unbind(String event) {
		return null;
	}

	@Override
	public List<Element> find(String selector) {
		return null;
	}

	@Override
	public Element unbind() {
		return null;
	}

	@Override
	public Element empty() {
		return null;
	}

	@Override
	public void remove() {
	}

	@Override
	public Element click() {
		return null;
	}

	@Override
	public Element getParent() {
		return null;
	}

	@Override
	public Input getInput() {
		return null;
	}

	@Override
	public CheckBox getCheckBox() {
		return null;
	}

	@Override
	public RadioButton getRadioButton() {
		return null;
	}

	@Override
	public Option getOption() {
		return null;
	}

	@Override
	public Form getForm() {
		return null;
	}

	@Override
	public Select getSelect() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public Element setId(String id) {
		return null;
	}

	@Override
	public Element append(String html) {
		return null;
	}

	@Override
	public Element append(Element element) {
		return null;
	}

	@Override
	public Element prepend(Element element) {
		return null;
	}

	@Override
	public Element after(String html) {
		return null;
	}

	@Override
	public Element after(Element element) {
		return null;
	}

	@Override
	public Element before(String html) {
		return null;
	}

	@Override
	public Element before(Element element) {
		return null;
	}

	@Override
	public Element prepend(String html) {
		return null;
	}

	@Override
	public String getInnerHTML() {
		return null;
	}

	@Override
	public String getOuterHTML() {
		return null;
	}

	@Override
	public Element setInnerHTML(String html) {
		return null;
	}

	@Override
	public boolean isHtmlElement() {
		return false;
	}

	@Override
	public Element setText(String text) {
		return null;
	}

	@Override
	public Element setTabIndex(int index) {
		return null;
	}

	@Override
	public int getTabIndex() {
		return 0;
	}

	@Override
	public Element focus() {
		return null;
	}

	@Override
	public Element query(String selector) {
		return null;
	}

	@Override
	public List<Element> queryAll(String selector) {
		return null;
	}

	@Override
	public Element on(String event, String selector, EventHandler handler) {
		return null;
	}

	@Override
	public Element off() {
		return null;
	}

	@Override
	public Element off(String event) {
		return null;
	}

	@Override
	public Element off(String event, String selector) {
		return null;
	}

	@Override
	public Point getOffset() {
		return null;
	}

	@Override
	public Point getPosition() {
		return null;
	}

	@Override
	public Element detach() {
		return null;
	}

	@Override
	public boolean isAttached() {
		return false;
	}

	@Override
	public Element scrollIntoView(boolean alignWithTop) {
		return null;
	}

	@Override
	public Element setCss(String propertyName, String value) {
		return null;
	}

	@Override
	public Element setCss(Map<String, String> properties) {
		return null;
	}

	@Override
	public Element removeCss(String propertyName) {
		return null;
	}

	@Override
	public Element setCss(String propertyName, String value, String important) {
		return null;
	}

	@Override
	public String getCss(String propertyName) {
		return null;
	}

	@Override
	public Element getPrev() {
		return null;
	}

	@Override
	public Element getNext() {
		return null;
	}

	@Override
	public boolean hasChildNodes() {
		return false;
	}

	@Override
	public Element appendTo(Element parent) {
		return null;
	}

	@Override
	public float getOuterHeight() {
		return 0;
	}

	@Override
	public float getClientHeight() {
		return 0;
	}

	@Override
	public float getClientWidth() {
		return 0;
	}

	@Override
	public float getOuterWidth() {
		return 0;
	}

	@Override
	public Element hide() {
		return null;
	}

	@Override
	public Element show() {
		return null;
	}

	@Override
	public Element cloneElement() {
		return null;
	}

	@Override
	public boolean contains(Element element) {
		return false;
	}

	@Override
	public boolean is(String selector) {
		return false;
	}

	@Override
	public Element getOffsetParent() {
		return null;
	}

	@Override
	public Element replaceWidth(String html) {
		return null;
	}

	@Override
	public Element replaceWidth(Element element) {
		return null;
	}

	@Override
	public List<Element> getSiblings(String selector) {
		return null;
	}

	@Override
	public List<Element> getSiblings() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Element getNextSibling() {
		return null;
	}

	@Override
	public Element closest(String selector) {
		return null;
	}
}
