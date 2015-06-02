package com.ui4j.webkit;

import com.ui4j.api.util.LoggerFactory;

import com.ui4j.api.util.Logger;

public class WebKitErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebKitErrorHandler.class);

    public void onError(String message, String url, int lineNumber) {
        LOG.error("javascript error: " + message + " " + url + ":" + lineNumber);
    }
}
