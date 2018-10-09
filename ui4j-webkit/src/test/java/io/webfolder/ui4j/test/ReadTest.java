package io.webfolder.ui4j.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.browser.PageConfiguration;
import io.webfolder.ui4j.api.dom.Element;

public class ReadTest extends AbstractFileServerTest {

    @Test @Ignore public void test() {
        Page page = BrowserFactory.getWebKit().navigate("http://localhost:58844/ReadTest.html",
                new PageConfiguration());
        List<Element> images = page.getDocument().queryAll("img");

        Element image1 = images.get(0);
        float w1 = image1.getClientWidth();
        float h1 = image1.getClientHeight();
        assertEquals(w1, 43.0f, 0);
        assertEquals(h1, 25.0f, 0);

        Element image2 = images.get(1);
        float w2 = image2.getClientWidth();
        float h2 = image2.getClientHeight();
        assertEquals(w2, 43.0f, 0);
        assertEquals(h2, 25.0f, 0);
    }
}
