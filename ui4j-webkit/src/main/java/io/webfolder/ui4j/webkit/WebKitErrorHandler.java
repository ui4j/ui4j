package io.webfolder.ui4j.webkit;

import io.webfolder.ui4j.api.util.Logger;
import io.webfolder.ui4j.api.util.LoggerFactory;

public class WebKitErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebKitErrorHandler.class);

    public void onError(String message, String url, int lineNumber) {
        LOG.error("javascript error: " + message + " " + url + ":" + lineNumber);
    }
}
