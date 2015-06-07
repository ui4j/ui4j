package com.ui4j.test;

import org.junit.Assert;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;
import com.ui4j.api.interceptor.Response;

public class HeaderTest {

    private static Response response;

    @Test
    public void testRequestInterceptor() throws Exception {
        BrowserEngine webKit = BrowserFactory.getWebKit();

        PageConfiguration config = new PageConfiguration(new Interceptor() {

            @Override
            public void beforeLoad(Request request) {
                request.setHeader("Foo", "bar");
            }

            @Override
            public void afterLoad(Response response) {
                HeaderTest.response = response;
            }
        });

        Page page = webKit.navigate("http://httpbin.org/get", config);

        String content = page.getWindow().getDocument().getBody().getText().get();
        JsonObject json = JsonObject.readFrom(content);
        JsonObject headers = json.get("headers").asObject();
        String bar = headers.get("Foo").asString();

        Assert.assertEquals("bar", bar);

        Assert.assertEquals("application/json", response.getHeader("Content-Type"));

        page.close();
    }
}
