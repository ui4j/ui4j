package io.webfolder.ui4j.spi;

public interface JavaScriptEngine {

    Object getEngine();

    Object executeScript(String script);
}
