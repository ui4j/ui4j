package io.webfolder.ui4j.api.event;

import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Window;

public class DocumentLoadEvent {

    private Window window;

    public DocumentLoadEvent(Window window) {
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }

    public Document getDocument() {
        if (window != null) {
            return window.getDocument();
        }
        return null;
    }

    @Override
    public String toString() {
        return "DocumentLoadEvent [window=" + window + "]";
    }
}
