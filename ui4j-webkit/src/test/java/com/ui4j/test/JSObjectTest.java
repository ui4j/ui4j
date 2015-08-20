package com.ui4j.test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.webkit.WebKitMapper;

import netscape.javascript.JSObject;

public class JSObjectTest {

    @Test
    @SuppressWarnings("unchecked")
    public void test() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(JSObjectTest.class.getResource("/JSObjectTest.html").toExternalForm())) {
            JSObject object = (JSObject) page.executeScript("myObject");
            Map<String, Object> map = new WebKitMapper(page).toJava(object);
            assertEquals(200, map.get("nmbr"));
            assertEquals(true, map.get("yes"));
            assertEquals(false, map.get("no"));
            List<Object> arr = (List<Object>) map.get("arr");
            assertEquals(5, arr.size());
            assertEquals(10, arr.get(0));
            assertEquals("my string", arr.get(1));
            assertEquals(true, arr.get(2));
            assertEquals(false, arr.get(3));
            Map<String, Object> keys = (Map<String, Object>) arr.get(4);
            assertEquals("value1", keys.get("key1"));
            List<Integer> numbers = (List<Integer>) keys.get("key2");
            assertEquals(1, (int) numbers.get(0));
            assertEquals(2, (int) numbers.get(1));
            assertEquals(3, (int) numbers.get(2));
            assertEquals(4, (int) numbers.get(3));
        }
    }
}
