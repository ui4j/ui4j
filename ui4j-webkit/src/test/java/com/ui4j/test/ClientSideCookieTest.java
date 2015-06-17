package com.ui4j.test;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

import static org.junit.Assert.assertEquals;

import com.sun.webkit.network.CookieManager;

public class ClientSideCookieTest extends AbstractFileServerTest {

    @Test
    public void test() throws IOException, InterruptedException {
    	CookieHandler.setDefault(new CookieManager());
        Page page = BrowserFactory.getWebKit().navigate("http://localhost:58844/ClientSideCookieTest.html");
        System.out.println(page.executeScript("typeof document"));
        System.out.println(page.executeScript("typeof document.cookie"));
        System.out.println(page.executeScript("document.readyState"));
        String cookie = (String) page.executeScript("document.cookie");
        System.out.println(cookie);
        String[] array = cookie.split(";");
        List<HttpCookie> cookies = new ArrayList<>();
        for (String next : array) {
            cookies.addAll(HttpCookie.parse(next));
        }
        assertEquals(2, cookies.size());
        assertEquals("foobar", cookies.get(0).getName());
        assertEquals("xyz", cookies.get(1).getName());
    }
}
