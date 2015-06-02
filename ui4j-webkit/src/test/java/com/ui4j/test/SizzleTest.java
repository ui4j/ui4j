package com.ui4j.test;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.browser.SelectorType;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class SizzleTest {

    public static Document document;

    @BeforeClass public static void beforeTest() {
        BrowserEngine browser = BrowserFactory.getWebKit();
        PageConfiguration configuration = new PageConfiguration();
        configuration.setSelectorEngine(SelectorType.SIZZLE);
        Page page = browser.navigate("about:blank", configuration);
        document = page.getDocument();
    }

    @Test public void test() {
    	Element body = document.getBody();
    	body.append("<div>foo<input>bar</input></div>");
    	Element div = document.query("body:first").get();
    	Assert.assertEquals("<div>foo<input>bar</div>", div.getInnerHTML());
    	List<Element> inputs = document.queryAll(":input");
    	Assert.assertEquals(1, inputs.size());
    	Element input = div.query(":input").get();
    	Assert.assertTrue(input.isSameNode(inputs.get(0)));
    	Assert.assertTrue(input.is(":input"));
    	List<Element> elements = div.queryAll(":last");
    	Assert.assertEquals(1, elements.size());
    	Assert.assertEquals("input", elements.get(0).getTagName());
    }
}
