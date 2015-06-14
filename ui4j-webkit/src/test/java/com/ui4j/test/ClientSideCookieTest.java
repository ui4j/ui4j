package com.ui4j.test;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import static org.junit.Assert.assertEquals;

public class ClientSideCookieTest extends AbstractFileServerTest {

    @Test
    public void test() throws IOException, InterruptedException {
        Page page = BrowserFactory.getWebKit().navigate("http://localhost:58844/ClientSideCookieTest.html");
        String cookie = (String) page.executeScript("document.cookie");
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
