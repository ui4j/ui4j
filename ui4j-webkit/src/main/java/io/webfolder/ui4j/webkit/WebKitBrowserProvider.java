package io.webfolder.ui4j.webkit;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserType;
import io.webfolder.ui4j.spi.BrowserProvider;
import io.webfolder.ui4j.spi.ShutdownListener;

public class WebKitBrowserProvider implements BrowserProvider {

    private static class NoOpShutdownListener implements ShutdownListener {

        @Override
        public void onShutdown(BrowserEngine engine) {
            // no op
        }
    }

    private ShutdownListener shutdownListener = new NoOpShutdownListener();

    @Override
    public BrowserType getBrowserType() {
        return BrowserType.WebKit;
    }

    @Override
    public BrowserEngine create() {
        return new WebKitBrowser(shutdownListener);
    }

    @Override
    public void setShutdownListener(ShutdownListener listener) {
        this.shutdownListener = listener;
    }
}
