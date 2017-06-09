package io.webfolder.ui4j.sample;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.browser.PageConfiguration;

public class UserAgent {

    public static void main(String[] args) {
        BrowserEngine webKit = BrowserFactory.getWebKit();

        PageConfiguration config = new PageConfiguration();
        config.setUserAgent("Custom User Agent String");

        Page page = webKit.navigate("http://httpbin.org/user-agent", config);
        page.show();
    }
}
