package io.webfolder.ui4j.test;

import org.junit.Test;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Input;

import static org.junit.Assert.assertTrue;

public class HiddenTest {

    @Test
    public void test() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(HiddenTest.class.getResource("/HiddenTest.html").toExternalForm())) {
            Document document = page.getDocument();
            Input input = document.query("input[name='b']").getInput();
            assertTrue(input.isHidden());
        }
    }
}
