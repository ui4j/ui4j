package com.ui4j.test;

import static com.ui4j.api.browser.BrowserFactory.getWebKit;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Element;

public class HeadTest {

    @Test
    public void test() {
        String url = ElementTest.class.getResource("/HeadTest.html").toExternalForm();
        BrowserEngine browser = getWebKit();
        Page page = browser.navigate(url);
        Element head = page.getWindow().getDocument().getHead();
        List<Element> list = head.find("[charset]");
        assertEquals(1, list.size());
        assertEquals("utf-8", list.get(0).getAttribute("charset").get());
    }
}
