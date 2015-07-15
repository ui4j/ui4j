package com.ui4j.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Element;

public class ImageOffsetTest {

    @Test
    public void test() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        String location = JSObjectTest.class.getResource("/ImageOffsetTest.html").toExternalForm();
        try (Page page = webkit.navigate(location)) {
            List<Element> elements = page.getDocument().queryAll("img");
            elements.forEach(e -> {
                System.out.println(e.getAttribute("alt").get() + " offset: " + e.getOffset());
            });
            Element image2 = elements.get(1);
            Assert.assertEquals(200, image2.getOffset().getLeft());
            Assert.assertEquals(200, image2.getOffset().getTop());
        }
    }
}
