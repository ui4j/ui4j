package com.ui4j.sample;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class GoogleSearch {

    public static void main(String[] args) throws Exception {
        BrowserEngine webkit = BrowserFactory.getWebKit();
		Page page = webkit.navigate("http://www.google.com");
        page.show();

        Document document = page.getDocument();

        document.query("input[name='q']").setAttribute("value", "java book").focus();

        // Send key press event using java.awt.Robot
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);

        page.waitUntilDocReady(1, TimeUnit.SECONDS);

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
