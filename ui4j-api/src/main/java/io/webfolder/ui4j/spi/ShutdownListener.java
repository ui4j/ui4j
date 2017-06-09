package io.webfolder.ui4j.spi;

import io.webfolder.ui4j.api.browser.BrowserEngine;

public interface ShutdownListener {

    void onShutdown(BrowserEngine engine);
}
