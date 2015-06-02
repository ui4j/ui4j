package com.ui4j.api.event;

import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Window;

public class DocumentLoadEvent {

    private Window window;

    public DocumentLoadEvent(Window window) {
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }

    public Document getDocument() {
        return window.getDocument();
    }

    @Override
    public String toString() {
        return "DocumentLoadEvent [window=" + window + "]";
    }
}
