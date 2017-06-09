package io.webfolder.ui4j.spi;

import io.webfolder.ui4j.api.dom.EventTarget;
import io.webfolder.ui4j.api.event.EventHandler;

public interface EventManager {

    void bind(EventTarget target, String event, EventHandler handler);

    void unbind(EventTarget target, EventHandler handler);

    void unbind(EventTarget target, String event);

    void unbind(EventTarget target);
}
