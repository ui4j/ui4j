package com.ui4j.sample;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

public class HackerNews {

    public static void main(String[] args) {

        try (Page page = BrowserFactory.getWebKit().navigate("https://news.ycombinator.com")) {
            page
                .getDocument()
                .queryAll(".title a")
                .forEach(e -> {
                    System.out.println(e.getText().get());
                });
        }

        BrowserFactory.getWebKit().shutdown();
    }
}
