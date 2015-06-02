package com.ui4j.api.event;

@FunctionalInterface
public interface EventHandler {

    void handle(DomEvent event);
}
