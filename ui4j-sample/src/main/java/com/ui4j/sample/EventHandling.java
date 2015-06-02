package com.ui4j.sample;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class EventHandling {

    public static void main(String[] args) {
        BrowserEngine browser = BrowserFactory.getWebKit();

        // navigate to blank page
        Page page = browser.navigate("about:blank");

        // show the browser window
        page.show();

        Document document = page.getDocument();
        Element body = document.getBody();

        body.append("<label>First Name</label><input id='txt' /><input type='button' value='click'></input>");

        // find the input element with an id #txt and bind the listener to the input event
        document.query("#txt").get().bind("input", (h) -> {
            System.out.println(h.getTarget().getValue());
        });

        // find the button and bind the click listener
        document.query("input[type='button']").get().bindClick((h) -> {
            System.out.println("clicked!");
        });

        // focus to text input
        document.query("#txt").get().focus();
    }
}
