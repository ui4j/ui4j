package com.ui4j.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

import static org.junit.Assert.assertEquals;

public class SiblingTest {

    @Test
    public void testGetSiblings() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(SiblingTest.class.getResource("/SiblingTest.html").toExternalForm())) {
            Document document = page.getDocument();
            List<Element> siblings = document.query(".b").get().getSiblings();
            assertEquals(2, siblings.size());
            assertEquals("a", siblings.get(0).getText().get());
            Assert.assertEquals("c", siblings.get(1).getText().get());
        }
    }

    @Test
    public void testGetSiblingWithSelector() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(SiblingTest.class.getResource("/SiblingTest.html").toExternalForm())) {
            Document document = page.getDocument();
            List<Element> siblings = document.query(".b").get().getSiblings("li");
            assertEquals(2, siblings.size());
            assertEquals("a", siblings.get(0).getText().get());
            Assert.assertEquals("c", siblings.get(1).getText().get());
        }
    }
}
