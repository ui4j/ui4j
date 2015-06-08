package com.ui4j.test;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.eclipsesource.json.JsonObject;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.Page;
import com.ui4j.webkit.WebKitBrowserProvider;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IsolatedSessionTest {

    private static BrowserEngine engine;

    @BeforeClass
    public static void setup() {
        WebKitBrowserProvider provider = new WebKitBrowserProvider();
        engine = provider.create();
    }

    @Test
    public void testFirstSession() {
        Page page = engine.navigate("http://httpbin.org/cookies/set?session1=value1");
        String session = page.getDocument().getBody().getText().get();
        JsonObject json = JsonObject.readFrom(session);
        JsonObject cookies = json.get("cookies").asObject();
        assertEquals(1, cookies.size());
        assertEquals("value1", cookies.get("session1").asString());

        engine.clearCookies();
    }

    @Test
    public void testSecondSession() {
        Page page = engine.navigate("http://httpbin.org/cookies/set?session2=value2");
        String session = page.getDocument().getBody().getText().get();
        JsonObject json = JsonObject.readFrom(session);
        JsonObject cookies = json.get("cookies").asObject();
        assertEquals(1, cookies.size());
        assertEquals("value2", cookies.get("session2").asString());
    }
}
