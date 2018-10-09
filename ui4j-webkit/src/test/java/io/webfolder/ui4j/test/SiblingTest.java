package io.webfolder.ui4j.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Element;

public class SiblingTest {

    @Test
    public void testGetSiblings() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(SiblingTest.class.getResource("/SiblingTest.html").toExternalForm())) {
            Document document = page.getDocument();
            List<Element> siblings = document.query(".b").getSiblings();
            assertEquals(2, siblings.size());
            assertEquals("a", siblings.get(0).getText());
            Assert.assertEquals("c", siblings.get(1).getText());
        }
    }

    @Test
    public void testGetSiblingWithSelector() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(SiblingTest.class.getResource("/SiblingTest.html").toExternalForm())) {
            Document document = page.getDocument();
            List<Element> siblings = document.query(".b").getSiblings("li");
            assertEquals(2, siblings.size());
            assertEquals("a", siblings.get(0).getText());
            Assert.assertEquals("c", siblings.get(1).getText());
        }
    }

    @Test
    public void testGetNextSibling() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(SiblingTest.class.getResource("/SiblingTest.html").toExternalForm())) {
            Document document = page.getDocument();
            Element sibling = document.query(".b").getNextSibling();
            Assert.assertEquals("c", sibling.getText());
        }
    }
}
