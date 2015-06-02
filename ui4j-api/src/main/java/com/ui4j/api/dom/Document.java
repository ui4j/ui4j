package com.ui4j.api.dom;

import java.util.List;
import java.util.Optional;

import com.ui4j.api.event.EventHandler;

public interface Document {

    Optional<Element> query(String selector);

    List<Element> queryAll(String selector);

    Element createElement(String tagName);

    void bind(String event, EventHandler handler);

    void unbind(String event);

    void unbind();

    Element getBody();

    void setTitle(String title);

    String getTitle();

    List<Element> parseHTML(String html);

    void trigger(String eventType, Element element);

    Element getElementFromPoint(int x, int y);
}
