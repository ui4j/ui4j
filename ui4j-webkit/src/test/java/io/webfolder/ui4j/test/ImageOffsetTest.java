package io.webfolder.ui4j.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Element;

public class ImageOffsetTest {

    @Test
    @Ignore
    public void test() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        String location = JSObjectTest.class.getResource("/ImageOffsetTest.html").toExternalForm();
        try (Page page = webkit.navigate(location)) {
            page.show();
            List<Element> elements = page.getDocument().queryAll("img");
            elements.forEach(e -> {
                System.out.println(e.getAttribute("alt") + " offset: " + e.getOffset());
            });
            Element element = page.getDocument().getElementFromPoint(200, 200);
            Assert.assertEquals("image 2", element.getAttribute("alt"));
        }
    }
}
