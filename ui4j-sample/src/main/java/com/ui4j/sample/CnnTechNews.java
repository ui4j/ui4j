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
        doc.query("#nav-tech").click();

        page.wait(1000); // tiny delay is important after click,
                         // if we dont delay waitUntilDocReady executed immediately without affect

        // wait until document ready
        page.waitUntilDocReady();

        // iterate the titles
        List<Element> techNews = doc.queryAll(".cnn_SRLTbbnstry1");

        techNews.forEach(n -> {
            Element next = n.getNext();
            List<Element> anchor = next.find("a");
            Element first = anchor.get(0);
            String text = first.getText();
            String newsTitle = text.trim();
            System.out.println(newsTitle);
        });

        browser.shutdown();
    }
}
