package com.ui4j.api.interceptor;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static java.util.Collections.unmodifiableMap;

import static java.util.Arrays.asList;

public class Request {

    private String url;

    private Map<String, List<String>> headers = new HashMap<>();

    public Request(String url) {
        this.url = url;
    }

    public Map<String, List<String>> getHeaders() {
        return unmodifiableMap(headers);
    }

    public String getUrl() {
        return url;
    }

    public Request setHeader(String name, String... values) {
        headers.put(name, asList(values));
        return this;
    }

    public Request setCookies(List<HttpCookie> cookies) {
        StringBuilder builder = new StringBuilder();
        for (HttpCookie cookie : cookies) {
            builder
                .append(cookie.getName())
                .append("=")
                .append(cookie.getValue())
                .append(";");
        }
        setHeader("Cookie", builder.toString());
        return this;
    }

    @Override
    public String toString() {
        return "Request [url=" + url + ", headers=" + headers + "]";
    }
}
