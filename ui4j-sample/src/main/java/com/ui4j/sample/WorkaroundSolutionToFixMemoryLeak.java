package com.ui4j.sample;

import java.io.IOException;
import java.net.URL;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;
import com.ui4j.api.interceptor.Response;

/**
 * Workaround solution to fix the WebView memory leaks
 * 
 * https://bugs.openjdk.java.net/browse/JDK-8087888
 *
 */
public class WorkaroundSolutionToFixMemoryLeak {

    public static class GifInterceptor implements Interceptor {

        @Override
        public void beforeLoad(Request request) {
            String url = request.getUrl();
            if (url.endsWith("gif")) {
                // serve empty image if requste is a gif image
                URL emptyImage = WorkaroundSolutionToFixMemoryLeak.class.getResource("/empty.jpg");
                try {
                    request.setUrlConnection(emptyImage.openConnection());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
        @Override
        public void afterLoad(Response response) {
            // no op
        }
    }

    public static void main(String[] args) {
        BrowserEngine webkit = BrowserFactory.getWebKit();

        String url = WorkaroundSolutionToFixMemoryLeak.class.getResource("/AnimatedGif.html").toExternalForm();

        Interceptor interceptor = new GifInterceptor();
        PageConfiguration configuration = new PageConfiguration(interceptor);

        // normally ui4j intercepts only html content
        // this flag help us to intercept all requests
        configuration.setInterceptAllRequests(true);

        Page page = webkit.navigate(url, configuration);

        page.show();
    }
}
