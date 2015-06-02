package com.ui4j.sample;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;

public class UserAgent {

    public static void main(String[] args) {
        BrowserEngine webKit = BrowserFactory.getWebKit();

        PageConfiguration config = new PageConfiguration();
        config.setUserAgent("Custom User Agent String");

        Page page = webKit.navigate("http://httpbin.org/user-agent", config);
        page.show();
    }
}
