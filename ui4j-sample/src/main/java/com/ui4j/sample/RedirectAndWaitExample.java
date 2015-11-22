package com.ui4j.sample;

import java.util.concurrent.CountDownLatch;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;
import com.ui4j.api.interceptor.Response;

public class RedirectAndWaitExample {

    public static void main(String[] args) {
        BrowserEngine engine = BrowserFactory.getWebKit();

        CountDownLatch latch = new CountDownLatch(1);

        Interceptor interceptor = new Interceptor() {

            @Override
            public void beforeLoad(Request request) {
                
            }
            
            @Override
            public void afterLoad(Response response) {
                if (response.getUrl().startsWith("https://www.google.com")) {
                    latch.countDown();
                }
            }
        };
        PageConfiguration configuration = 
                            new PageConfiguration(interceptor);
        Page page = engine.navigate("https://httpbin.org/redirect-to?url=https://www.google.com", configuration);

        try {
            // wait until redirect
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(page.getDocument().getTitle().get());

        page.close();

        BrowserFactory.getWebKit().shutdown();
    }
}
