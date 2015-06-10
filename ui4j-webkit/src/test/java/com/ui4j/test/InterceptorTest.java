package com.ui4j.test;

import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;
import com.ui4j.api.interceptor.Response;

public class InterceptorTest {

    @Test
    public void test() {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        PageConfiguration configuration = new PageConfiguration(new Interceptor() {

            @Override
            public void beforeLoad(Request request) {
                // no op
            }
            
            @Override
            public void afterLoad(Response response) {
                // no op
            }
        });
        try (Page page = webkit.navigate(SiblingTest.class.getResource("/SiblingTest.html").toExternalForm(), configuration)) {
            // no op
        }
    }
}
