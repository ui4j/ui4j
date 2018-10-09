package io.webfolder.ui4j.test;

import static org.junit.Assert.assertEquals;

import java.net.CookieHandler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;

import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.webkit.WebKitIsolatedCookieHandler;

public class IsolatedSessionTest {


    @BeforeClass
    public static void before() {
        CookieHandler.setDefault(new WebKitIsolatedCookieHandler());
    }
    
    @AfterClass
    public static void after() {
        CookieHandler.setDefault(null);
    }

    @Test
    public void test() {
        Page session1 = BrowserFactory.getWebKit().navigate("http://httpbin.org/cookies/set?session1=value1");
        String content1 = session1.getDocument().getBody().getText();
        JsonObject json1 = JsonObject.readFrom(content1);
        JsonObject cookies1 = json1.get("cookies").asObject();

        assertEquals(1, cookies1.size());
        assertEquals("value1", cookies1.get("session1").asString());

        Page session2 = BrowserFactory.getWebKit().navigate("http://httpbin.org/cookies/set?session2=value2");
        String content2 = session2.getDocument().getBody().getText();
        JsonObject json2 = JsonObject.readFrom(content2);
        JsonObject cookies2 = json2.get("cookies").asObject();

        assertEquals(1, cookies2.size());
        assertEquals("value2", cookies2.get("session2").asString());

        session1.close();
        session2.close();
    }
}
