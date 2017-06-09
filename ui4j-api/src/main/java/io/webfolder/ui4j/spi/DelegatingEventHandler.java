package io.webfolder.ui4j.spi;

import io.webfolder.ui4j.api.event.DomEvent;
import io.webfolder.ui4j.api.event.EventHandler;

public class DelegatingEventHandler implements EventHandler {

    private String event;

    private String selector;

    private EventHandler eventHandler;

    public DelegatingEventHandler(String event, String selector, EventHandler eventHandler) {
        this.event = event;
        this.selector = selector;
        this.eventHandler = eventHandler;
    }

    @Override
    public void handle(DomEvent event) {
        if (event.getTarget().is(selector)) {
            eventHandler.handle(event);
        }
    }

    public String getSelector() {
        return selector;
    }

    public EventHandler getHandler() {
        return eventHandler;
    }

    public String getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "DelegatingEventHandler [event=" + event + ", selector="
                + selector + ", eventHandler=" + eventHandler + "]";
    }
}
