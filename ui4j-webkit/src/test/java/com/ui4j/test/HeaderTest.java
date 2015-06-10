package com.ui4j.test;

import org.junit.Assert;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
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
                request.setHeader("Multi-Value-Header", "value1", "value2");
            }

            @Override
            public void afterLoad(Response response) {
                System.out.println(response.getHeaders());
                HeaderTest.response = response;
            }
        });

        Page page = webKit.navigate("http://httpbin.org/get", config);

        String content = page.getWindow().getDocument().getBody().getText().get();
        JsonObject json = JsonObject.readFrom(content);
        JsonObject headers = json.get("headers").asObject();
        String bar = headers.get("Foo").asString();
        String multiValue = headers.get("Multi-Value-Header").asString();

        System.out.println(content);

        Assert.assertEquals("bar", bar);

        Assert.assertEquals("value1,value2", multiValue);

        Assert.assertEquals("application/json", response.getHeader("Content-Type").get());

        page.close();
    }
}
