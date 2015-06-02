package com.ui4j.test;

import org.junit.Assert;
import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;

public class UserAgentTest {

    @Test
    public void test() {
        BrowserEngine webKit = BrowserFactory.getWebKit();

        PageConfiguration config = new PageConfiguration();
        config.setUserAgent("Custom User Agent String");

        Page page = webKit.navigate("http://httpbin.org/user-agent", config);
        page.show();

        Assert.assertTrue(page.getDocument().getBody().getInnerHTML().contains(config.getUserAgent()));
    }
}
