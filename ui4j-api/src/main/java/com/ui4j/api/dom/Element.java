package com.ui4j.api.dom;

import java.util.List;
import java.util.Map;

import com.ui4j.api.event.EventHandler;
import com.ui4j.api.util.Point;

public interface Element extends Node {

    String getAttribute(String name);

    Element setAttribute(String name, String value);

    Element setAttribute(Map<String, String> attributes);

    Element removeAttribute(String name);

    boolean hasAttribute(String name);

    Element addClass(String... names);

    Element removeClass(String... names);

    boolean hasClass(String name);

    Element toggleClass(String name);

    List<String> getClasses();

    String getText();

    String getTagName();

    String getValue();

    Element setValue(String value);

    Element bind(String event, EventHandler handler);

    Element bindClick(EventHandler handler);

    Element setTitle(String title);

    String getTitle();

    Element unbind(EventHandler handler);

    Element unbind(String event);

    List<Element> find(String selector);

    Element unbind();

    Element empty();

    void remove();

    Element click();

    Element getParent();

    Input getInput();

    CheckBox getCheckBox();

    RadioButton getRadioButton();

    Option getOption();

    Form getForm();

    Select getSelect();

    String getId();

    Element setId(String id);

    Element append(String html);

    Element append(Element element);

    Element prepend(Element element);

    Element after(String html);

    Element after(Element element);

    Element before(String html);

    Element before(Element element);

    Element prepend(String html);

    String getInnerHTML();

    String getOuterHTML();

    Element setInnerHTML(String html);

    boolean isHtmlElement();

    Element setText(String text);

    Element setTabIndex(int index);

    int getTabIndex();

    Element focus();

    Element query(String selector);

    List<Element> queryAll(String selector);

    Element on(String event, String selector, EventHandler handler);

    Element off();

    Element off(String event);

    Element off(String event, String selector);

    Point getOffset();

    Point getPosition();

    Element detach();

    boolean isAttached();

    Element scrollIntoView(boolean alignWithTop);

    Element setCss(String propertyName, String value);

    Element setCss(Map<String, String> properties);

    Element removeCss(String propertyName);

    Element setCss(String propertyName, String value, String important);

    String getCss(String propertyName);

    Element getPrev();

    Element getNext();

    boolean hasChildNodes();

    Element appendTo(Element parent);

    float getOuterHeight();

    float getClientHeight();

    float getClientWidth();

    float getOuterWidth();

    Element hide();

    Element show();    

    Element cloneElement();

    boolean contains(Element element);

    boolean is(String selector);

    Element getOffsetParent();

    Element replaceWith(String html);

    Element replaceWith(Element element);

    List<Element> getSiblings(String selector);

    List<Element> getSiblings();

    boolean isEmpty();

    Element getNextSibling();

    Element closest(String selector);

    Document getContentDocument();
}
