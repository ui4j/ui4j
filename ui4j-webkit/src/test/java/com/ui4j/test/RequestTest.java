package com.ui4j.test;

import java.net.HttpCookie;

import org.junit.Test;

import com.eclipsesource.json.JsonObject;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;
import com.ui4j.api.interceptor.Response;

import static org.junit.Assert.assertEquals;
import static java.util.Arrays.asList;

import static org.junit.Assert.assertTrue;

public class RequestTest {

    @Test
    public void testRemoveCookie() {
        Request request = new Request("about:blank");

        HttpCookie foo = new HttpCookie("foo", "bar");
        HttpCookie sessionId = new HttpCookie("JSESSIONID", "1");

        request.setCookies(asList(foo, sessionId));
        
        assertEquals(2, request.getCookies().size());
        assertEquals("bar", request.getCookies().get(0).getValue());
        assertEquals("1", request.getCookies().get(1).getValue());

        request.removeCookie("foo");
        assertEquals(1, request.getCookies().size());
        assertEquals("1", request.getCookies().get(0).getValue());
    }

    @Test
    public void testRemoveHeader() {
        Request request = new Request("about:blank");
        request.setHeader("foo", "bar");
        assertEquals("bar", request.getHeaders().get("foo").get(0));
        assertTrue(request.removeHeader("foo"));
    }

    @Test
    public void testCookies() {
        Page page = BrowserFactory.getWebKit().navigate("http://httpbin.org/cookies", new PageConfiguration(new Interceptor() {

            @Override
            public void beforeLoad(Request request) {
                request.setCookies(asList(new HttpCookie("foo", "bar"), new HttpCookie("JSESSIONID", "1")));
            }

            @Override
            public void afterLoad(Response response) {
            }
        }));

        String content = page.getDocument().getBody().getText().get();

        JsonObject json = JsonObject.readFrom(content);
        JsonObject cookies = json.get("cookies").asObject();

        assertEquals("1", cookies.get("JSESSIONID").asString());
        assertEquals("bar", cookies.get("foo").asString());
    }
}
