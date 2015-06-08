package com.ui4j.sample;

import java.util.List;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class CnnTechNews {

    public static void main(String[] args) {
        BrowserEngine browser = BrowserFactory.getBrowser(BrowserType.WebKit);

        // naviage to cnn
        Page page = browser.navigate("http://edition.cnn.com");

        Document doc = page.getDocument();

        // navigate to tech news
        doc.query("[href=\"/tech\"]").get().click();

        // iterate the titles
        List<Element> techNews = doc.queryAll(".cd__headline-text");

        techNews.forEach(n -> {
            System.out.println(n.getText().get());
        });

        browser.shutdown();
    }
}
