package com.ui4j.sample;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

public class JavaScriptExecution {

    public static void main(String[] args) {
        BrowserEngine webKit = BrowserFactory.getWebKit();
        Page page = webKit.navigate("about:blank");

        Object result = page.executeScript("2 + 2");
        System.out.println(result);

        page.close();
        webKit.shutdown();
    }
}
