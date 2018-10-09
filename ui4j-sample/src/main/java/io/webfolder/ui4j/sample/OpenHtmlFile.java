package io.webfolder.ui4j.sample;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;

public class OpenHtmlFile {

    public static void main(String[] args) {
        // Load sample file from classpath and get its URL location
        String url = OpenHtmlFile.class.getResource("/sample-page.html").toExternalForm();

        // get the instance of WebKit browser
        BrowserEngine browser = BrowserFactory.getWebKit();

        // navigate to url & show the browser page
        browser.navigate(url).show();
    }
}
