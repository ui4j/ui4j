package com.ui4j.sample;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.browser.SelectorType;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class Sizzle {

    public static void main(String[] args) {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        PageConfiguration configuration = new PageConfiguration();
        configuration.setSelectorEngine(SelectorType.SIZZLE);
        Page page = webkit.navigate("http://sizzlejs.com", configuration);
        Document document = page.getDocument();

        Element element = document.query("h1:contains('Sizzle JavaScript Selector Library')").get();

        System.out.println(element.getInnerHTML());

        page.close();
        webkit.shutdown();
    }
}
