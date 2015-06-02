package com.ui4j.api.browser;

import java.io.OutputStream;

import com.ui4j.api.dialog.AlertHandler;
import com.ui4j.api.dialog.ConfirmHandler;
import com.ui4j.api.dialog.PromptHandler;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Window;
import com.ui4j.api.event.DocumentListener;
import com.ui4j.spi.JavaScriptEngine;
import com.ui4j.spi.PageView;

public interface Page extends JavaScriptEngine, PageView, AutoCloseable {

    void addDocumentListener(DocumentListener listener);

    void removeListener(DocumentListener listener);

    Document getDocument();

    Window getWindow();

    void show(boolean maximized);

    void show();

    void hide();

    void setAlertHandler(AlertHandler handler);

    void setPromptHandler(PromptHandler handler);

    void setConfirmHandler(ConfirmHandler handler);

    BrowserType getBrowserType();

    void close();

    void captureScreen(OutputStream os);
}
