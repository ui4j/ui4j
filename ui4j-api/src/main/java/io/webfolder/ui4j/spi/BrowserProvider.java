package io.webfolder.ui4j.spi;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserType;

public interface BrowserProvider {

    BrowserType getBrowserType();

    BrowserEngine create();

    void setShutdownListener(ShutdownListener listener);
}
