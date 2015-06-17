package com.ui4j.webkit.spi;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

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

        String resultStr = String.valueOf(result);

        try {
            NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
            ParsePosition pos = new ParsePosition(0);
            Number number = formatter.parse(resultStr, pos);
            if (number != null) {
                if (resultStr.length() == pos.getIndex()) {
                    return number;
                }
            }
        } catch (Throwable t) {
            // ignore issue #55
        }

        return result;
    }
}
