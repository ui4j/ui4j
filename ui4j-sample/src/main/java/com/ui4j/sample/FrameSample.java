package com.ui4j.sample;

import java.util.List;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class FrameSample {

    public static void main(String[] args) {
        // Load sample file from classpath and get its URL location
        String url = FrameSample.class.getResource("/Frame.html").toExternalForm();

        // get the instance of WebKit browser
        BrowserEngine browser = BrowserFactory.getWebKit();

        // navigate to url & show the browser page
        Page page = browser.navigate(url);
        page.show();

        List<Element> frames = page.getDocument().queryAll("frame");

        Document documentFrame1 = frames.get(0).getContentDocument().get();
        Document documentFrame2 = frames.get(1).getContentDocument().get();

        System.out.println(documentFrame1.query("div"));
        System.out.println(documentFrame2.query("div"));
    }
}
