package com.ui4j.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;
import com.sun.webkit.network.CookieManager;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

public class IsolatedSessionTest {

    private static class ThreadLocalCookieManager extends CookieHandler {

        private static final ThreadLocal<CookieManager> cookieManager = new ThreadLocal<CookieManager>() {

            @Override
            protected CookieManager initialValue() {
                return new CookieManager();
            }
        };

        @Override
        public Map<String, List<String>> get(URI uri,
                Map<String, List<String>> requestHeaders) throws IOException {
            return cookieManager.get().get(uri, requestHeaders);
        }

        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders)
                throws IOException {
            cookieManager.get().put(uri, responseHeaders);
        }
        
    }

    private static String defaulMaxConnections;

    @BeforeClass
    public static void before() {
        // Without this paramater we are unable isolate the cookies per page
        // @see com.sun.webkit.network.NetworkContext.fwkGetMaximumHTTPConnectionCountPerHost()
        defaulMaxConnections = System.getProperty("http.maxConnections");
        // default is 5 @see https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html
        // @see NetworkContext.DEFAULT_HTTP_MAX_CONNECTIONS
        System.setProperty("http.maxConnections", "1");
        CookieHandler.setDefault(new ThreadLocalCookieManager());
    }
    
    @AfterClass
    public static void after() {
        if (defaulMaxConnections != null) {
            System.setProperty("http.maxConnections", defaulMaxConnections);
        } else {
            System.setProperty("http.maxConnections", "5");
        }
        CookieHandler.setDefault(null);
    }

    @Test
    public void test() {
        Page session1 = BrowserFactory.getWebKit().navigate("http://httpbin.org/cookies/set?session1=value1");
        String content1 = session1.getDocument().getBody().getText().get();
        JsonObject json1 = JsonObject.readFrom(content1);
        JsonObject cookies1 = json1.get("cookies").asObject();

        assertEquals(1, cookies1.size());
        assertEquals("value1", cookies1.get("session1").asString());

        Page session2 = BrowserFactory.getWebKit().navigate("http://httpbin.org/cookies/set?session2=value2");
        String content2 = session2.getDocument().getBody().getText().get();
        JsonObject json2 = JsonObject.readFrom(content2);
        JsonObject cookies2 = json2.get("cookies").asObject();

        assertEquals(1, cookies2.size());
        assertEquals("value2", cookies2.get("session2").asString());

        session1.close();
        session2.close();
    }
}
