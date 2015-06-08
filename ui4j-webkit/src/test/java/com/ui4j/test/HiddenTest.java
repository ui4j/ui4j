package com.ui4j.test;

import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Input;


import static org.junit.Assert.assertTrue;

public class HiddenTest {

    @Test
    public void test() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(HiddenTest.class.getResource("/HiddenTest.html").toExternalForm())) {
            Document document = page.getDocument();
            Input input = document.query("input[name='b']").get().getInput().get();
            assertTrue(input.isHidden());
        }
    }
}
