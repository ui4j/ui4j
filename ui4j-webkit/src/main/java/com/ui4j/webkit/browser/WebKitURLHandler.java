package com.ui4j.webkit.browser;

import static java.lang.String.join;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;

public class WebKitURLHandler extends URLStreamHandler {

    private String context;

    private static final String UI4J_PROTOCOL = "ui4j";

    private Interceptor interceptor;
    
    private URLConnection contextConnection;

    private CookieHandler cookieHandler;

    private AtomicInteger requestCounter = new AtomicInteger(0);

    public WebKitURLHandler(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        int rcount = requestCounter.incrementAndGet();

        String protocol = u.getProtocol();

        if (!protocol.startsWith(UI4J_PROTOCOL)) {
            return null;
        }

        // url without ui4j prefix
        String url = u.toString().substring(protocol.length() + 1, u.toString().length());

        if (context == null && url.startsWith("http")) {
            context = url;
        }

        if (context != null &&
                        !url.startsWith("http") &&
                            !url.startsWith("/") &&
                            !context.endsWith("/")) {
            String f = u.getFile().replaceAll("https://", "");
            url = context + "/" + f;
        }

        URLConnection connection = new URL(url).openConnection();

        if (rcount == 1) {
            contextConnection = connection;
        }

        Request request = new Request(url);

        if (rcount == 1) { // apply the interceptor for only first request
            interceptor.beforeLoad(request);
        }

        if (request != null) {
            for (Map.Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
                String key = entry.getKey();
                String value = join(",", entry.getValue());
                connection.setRequestProperty(key, value);
            }
        }
        
        return connection;
    }

    public URLConnection getConnection() {
        return contextConnection;
    }

    public CookieHandler getCookieHandler() {
        return cookieHandler;
    }
}
