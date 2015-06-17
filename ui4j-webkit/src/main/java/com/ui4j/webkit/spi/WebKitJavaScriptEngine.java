package com.ui4j.webkit.spi;

import javafx.scene.web.WebEngine;

import com.ui4j.spi.JavaScriptEngine;

public class WebKitJavaScriptEngine implements JavaScriptEngine {

    private WebEngine engine;

    public WebKitJavaScriptEngine(WebEngine engine) {
        this.engine = engine;
    }

    @Override
    public WebEngine getEngine() {
        return engine;
    }

    @Override
    public Object executeScript(String script) {
        Object result = engine.executeScript(script);
        return result;
    }
}
