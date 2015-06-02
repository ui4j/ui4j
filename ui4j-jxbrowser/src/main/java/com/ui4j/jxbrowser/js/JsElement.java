package com.ui4j.jxbrowser.js;

import com.teamdev.jxbrowser.chromium.JSObject;

public interface JsElement extends JsObject {

    @JsProperty("childNodes")
    JsNodeList getChildNodes();

    @JsProperty("nodeType")
    short getNodeType();

    @JsProperty("innerHTML")
    String getInnerHTML();

    @JsProperty("innerHTML")
    void setInnerHTML(String html);

    @JsProperty("outerHTML")
    String getOuterHTML();

    @JsProperty("title")
    String getTitle();

    @JsProperty("title")
    void setTitle(String title);

    @JsProperty("textContent")
    String getTextContent();

    @JsProperty("textContent")
    void setText(String text);

    @JsProperty("tagName")
    String geTagName();

    @JsProperty("value")
    String getValue();

    @JsProperty("value")
    void setValue(String value);

    @JsProperty("id")
    String getId();

    @JsProperty("id")
    void setId(String id);

    @JsProperty("tabIndex")
    void setTabIndex(int index);

    @JsProperty("tabIndex")
    int getTabIndex();

    @JsProperty("clientWidth")
    float getClientWidth();

    @JsProperty("clientHeight")
    float getClientHeight();

    @JsProperty("style")
    void setStyle(String property, String value);

    @JsProperty("style")
    String getStyle(String property);

    @JsProperty("previousElementSibling")
    JsElement getPreviousElementSibling();

    @JsProperty("nextElementSibling")
    JsElement getNextElementSibling();

    @JsProperty("parentNode")
    JsElement getParentNode();

    @JsFunction("click")
    void click();

    @JsProperty("classList")
    JsDomTokenList getClassList();

    @JsFunction("getAttribute")
    String getAttribute(String name);

    @JsFunction("setAttribute")
    void setAttribute(String name, String value);

    @JsFunction("removeAttribute")
    void removeAttribute(String name);

    @JsFunction("hasAttribute")
    boolean hasAttribute(String name);

    @JsFunction("appendChild")
    void appendChild(JSObject object);

    @JsFunction("removeChild")
    void removeChild(JSObject object);

    @JsFunction("focus")
    void focus();

    @JsFunction("querySelector")
    JsElement querySelector(String selector);

    @JsFunction("querySelectorAll")
    JsNodeList querySelectorAll(String selector);
}
