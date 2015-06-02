package com.ui4j.api.event;

@FunctionalInterface
public interface DocumentListener {

    void onLoad(DocumentLoadEvent event);
}
