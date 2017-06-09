package io.webfolder.ui4j.webkit.dom;

import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Window;
import javafx.scene.web.WebEngine;

public class WebKitWindow implements Window {

    private Document document;

    private WebEngine engine;

    public WebKitWindow(Document document) {
        this.document = document;
        WebKitDocument documentImpl = (WebKitDocument) document;
        this.engine = documentImpl.getEngine();
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public String getLocation() {
        String location = engine.getLocation();
        if (location.startsWith("ui4j-")) {
            location = location.substring(location.indexOf(":") + 1, location.length());
        }
        return location;
    }

    @Override
    public void setLocation(String location) {
        engine.executeScript(String.format("window.location.href='%s'", location));
    }

    @Override
    public void back() {
        engine.executeScript("window.history.back()");
    }

    @Override
    public void forward() {
        engine.executeScript("window.history.forward()");
    }

    @Override
    public void reload() {
        engine.executeScript("window.location.reload()");
    }
}
