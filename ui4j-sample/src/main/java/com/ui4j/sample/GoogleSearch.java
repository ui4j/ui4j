package com.ui4j.sample;

import java.net.URLEncoder;
import java.util.List;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class GoogleSearch {

    public static void main(String[] args) throws Exception {

        String keyword = "java books";
        String encodedKeyword = URLEncoder.encode(keyword, "utf-8");
        String url = String.format("http://www.google.com.tr/?l#q=%s", encodedKeyword);

        BrowserEngine webkit = BrowserFactory.getWebKit();
        Page page = webkit.navigate(url);
        page.waitUntilDocReady();

        page.show();

        Document document = page.getDocument();

        // list all search results
        List<Element> results = document.queryAll("h3.r a");

        // visit the first result page
        results.get(0).click();

        // wait until page load
        page.waitUntilDocReady();

        // extract title, location, content etc. from result page
        System.out.println(String.format("Title: %s, Location: %s",
                page.getDocument().getTitle(),
                page.getWindow().getLocation()));

        page.close();
        webkit.shutdown();
    }
}
