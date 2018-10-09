package io.webfolder.ui4j.api.browser;

import java.util.List;

import io.webfolder.ui4j.api.dom.Element;

public interface SelectorEngine {

    Element query(String selector);

    List<Element> queryAll(String selector);

    Element query(Element element, String selector);

    List<Element> queryAll(Element element, String selector);
}
