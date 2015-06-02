package com.ui4j.test;

import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Element;

import static org.junit.Assert.assertEquals;

public class SetInnerHTMLTest {

	@Test
	public void test() throws InterruptedException {
		BrowserEngine webkit = BrowserFactory.getWebKit();
		try (Page page = webkit.navigate(ChildTest.class.getResource("/SetInnerTest.html").toExternalForm())) {
			Element withInner = page.getDocument().query("#with-inner").get();
			withInner.setInnerHTML("<span id=\"blub\"></span>");
			String innerHTML = withInner.getInnerHTML();
			assertEquals(innerHTML, "<span id=\"blub\"></span>");
		}
	}
}
