package io.webfolder.ui4j.sample;

import static io.webfolder.ui4j.api.browser.BrowserFactory.getWebKit;

import io.webfolder.ui4j.api.browser.Page;

public class HackerNews {

    public static void main(String[] args) {

        try (Page page = getWebKit().navigate("https://news.ycombinator.com")) {
            page
                .getDocument()
                .queryAll(".title a")
                .forEach(e -> {
                    System.out.println(e.getText());
                });
        }
    }
}
