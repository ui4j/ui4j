package io.webfolder.ui4j.api.dom;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.webfolder.ui4j.api.event.EventHandler;
import io.webfolder.ui4j.api.util.Point;

public interface Element extends Node {

    Optional<String> getAttribute(String name);

    Element setAttribute(String name, String value);

    Element setAttribute(Map<String, String> attributes);

    Element removeAttribute(String name);

    boolean hasAttribute(String name);

    Element addClass(String... names);

    Element removeClass(String... names);

    boolean hasClass(String name);

    Element toggleClass(String name);

    List<String> getClasses();

    Optional<String> getText();

    String getTagName();

    Optional<String> getValue();

    Element setValue(String value);

    Element bind(String event, EventHandler handler);

    Element bindClick(EventHandler handler);

    Element setTitle(String title);

    Optional<String> getTitle();

    Element unbind(EventHandler handler);

    Element unbind(String event);

    List<Element> find(String selector);

    Element unbind();

    Element empty();

    void remove();

    Element click();

    Optional<Element> getParent();

    Optional<Input> getInput();

    Optional<CheckBox> getCheckBox();

    Optional<RadioButton> getRadioButton();

    Optional<Option> getOption();

    Optional<Form> getForm();

    Optional<Select> getSelect();

    Optional<String> getId();

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

    Optional<Element> query(String selector);

    List<Element> queryAll(String selector);

    Element on(String event, String selector, EventHandler handler);

    Element off();

    Element off(String event);

    Element off(String event, String selector);

    Point getOffset();

    Optional<Point> getPosition();

    Element detach();

    boolean isAttached();

    Element scrollIntoView(boolean alignWithTop);

    Element setCss(String propertyName, String value);

    Element setCss(Map<String, String> properties);

    Element removeCss(String propertyName);

    Element setCss(String propertyName, String value, String important);

    Optional<String> getCss(String propertyName);

    Optional<Element> getPrev();

    Optional<Element> getNext();

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

    Optional<Element> getOffsetParent();

    Element replaceWith(String html);

    Element replaceWith(Element element);

    List<Element> getSiblings(String selector);

    List<Element> getSiblings();

    Optional<Element> getNextSibling();

    Optional<Element> closest(String selector);

    Optional<Document> getContentDocument();

    Object eval(String expression);
}
