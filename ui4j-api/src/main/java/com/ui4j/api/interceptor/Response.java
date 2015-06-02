package com.ui4j.api.interceptor;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Response {

    private String url;

    private Map<String, List<String>> headers;

    public Response(String url, Map<String, List<String>> headers) {
        this.url = url;
        this.headers = headers;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public HttpCookie getCookie(String name) {
        for (HttpCookie cookie : getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    public List<HttpCookie> getCookies() {
        List<HttpCookie> cookies = new ArrayList<>();
        if (headers != null) {
            List<String> list = headers.get("Set-Cookie");
            if (list != null) {
                for (String next : list) {
                    List<HttpCookie> hc = HttpCookie.parse(next);
                    if (!hc.isEmpty()) {
                        cookies.add(hc.get(0));
                    }
                }
            }
        }
        return Collections.unmodifiableList(cookies);
    }

    public String getHeader(String name) {
        List<String> values = headers.get(name);
        if (values != null && !values.isEmpty()) {
            return values.get(0);
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Response [url=" + url + ", headers=" + headers + "]";
    }
}
