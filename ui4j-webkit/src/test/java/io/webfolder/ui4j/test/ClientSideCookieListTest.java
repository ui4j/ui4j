package io.webfolder.ui4j.test;

import static org.junit.Assert.assertEquals;

import java.net.CookieHandler;

import org.junit.Test;

import com.sun.webkit.network.CookieManager;

import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;

public class ClientSideCookieListTest extends AbstractFileServerTest {

    @Test
    public void test() {
        CookieHandler.setDefault(new CookieManager());
        Page page = BrowserFactory.getWebKit().navigate("http://localhost:58844/ClientSideCookieList.html");
        String cookie = page.getDocument().query("#cookies").get().getChildren().get(0).getText().get();
        String cookieName = cookie.split("=")[0];
        assertEquals("foobar", cookieName);
        assertEquals("foobar", ((String) page.executeScript("document.cookie")).split("=")[0]);
    }
}
