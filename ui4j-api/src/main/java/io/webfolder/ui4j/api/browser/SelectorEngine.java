package io.webfolder.ui4j.api.browser;

import java.util.List;
import java.util.Optional;

import io.webfolder.ui4j.api.dom.Element;

public interface SelectorEngine {

    Optional<Element> query(String selector);

    List<Element> queryAll(String selector);

    Optional<Element> query(Element element, String selector);

    List<Element> queryAll(Element element, String selector);
}
