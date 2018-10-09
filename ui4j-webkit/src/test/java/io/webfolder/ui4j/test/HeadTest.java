package io.webfolder.ui4j.test;

import static io.webfolder.ui4j.api.browser.BrowserFactory.getWebKit;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Element;

public class HeadTest {

    @Test
    public void test() {
        String url = ElementTest.class.getResource("/HeadTest.html").toExternalForm();
        BrowserEngine browser = getWebKit();
        Page page = browser.navigate(url);
        Element head = page.getWindow().getDocument().getHead();
        List<Element> list = head.find("[charset]");
        assertEquals(1, list.size());
        assertEquals("utf-8", list.get(0).getAttribute("charset"));
    }
}
