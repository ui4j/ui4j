package com.ui4j.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;
import com.ui4j.api.interceptor.Response;

import static org.junit.Assert.assertTrue;

public class InterceptorTest {

    @Test
    public void testOffline() {
        String url = getClass().getResource("/ChildTest.html").toExternalForm();

        AtomicInteger beforeCounter = new AtomicInteger();
        AtomicInteger afterCounter = new AtomicInteger();

        StringBuilder builder = new StringBuilder();

        BrowserFactory.getWebKit().navigate(url, new PageConfiguration(new Interceptor() {

            @Override
            public void beforeLoad(Request request) {
                builder.append(request.getUrl());
                beforeCounter.incrementAndGet();
            }

            @Override
            public void afterLoad(Response response) {
                afterCounter.incrementAndGet();
            }
        }));

        assertTrue(builder.toString().endsWith("ChildTest.html"));
        assertEquals(1, beforeCounter.get());
        assertEquals(1, afterCounter.get());
    }

    @Test
    public void testOnline() {
        String url = "http://www.oracle.com";

        AtomicInteger beforeCounter = new AtomicInteger();
        AtomicInteger afterCounter = new AtomicInteger();

        StringBuilder builder = new StringBuilder();

        BrowserFactory.getWebKit().navigate(url, new PageConfiguration(new Interceptor() {

            @Override
            public void beforeLoad(Request request) {
                builder.append(request.getUrl());
                beforeCounter.incrementAndGet();
            }

            @Override
            public void afterLoad(Response response) {
                afterCounter.incrementAndGet();
            }
        }));

        assertEquals(url, builder.toString());
        assertEquals(1, beforeCounter.get());
        assertEquals(1, afterCounter.get());
    }
}
