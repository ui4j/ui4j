package com.ui4j.api.interceptor;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.Optional;

import static java.util.Collections.unmodifiableMap;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Request {

    private String url;

    private Map<String, List<String>> headers = new HashMap<>();

    public Request(String url) {
        this.url = url;
    }

    public Map<String, List<String>> getHeaders() {
        return unmodifiableMap(headers);
    }

    public Optional<String> getHeader(String name) {
        List<String> values = headers.get(name);
        if (values != null && !values.isEmpty()) {
            return Optional.of(values.get(0));
        }
        return Optional.empty();
    }

    public String getUrl() {
        return url;
    }

    public Request setHeader(String name, String... values) {
        headers.put(name, asList(values));
        return this;
    }

    public Request setCookie(HttpCookie cookie) {
        List<HttpCookie> cookies = new ArrayList<>();
        cookies.add(cookie);
        setCookies(cookies);
        return this;
    }

    public boolean removeHeader(String name) {
        List<String> list = headers.remove(name);
        return list != null ? true : false;
    }

    public boolean removeHeaders(List<String> names) {
        boolean removed = false;
        if (names != null) {
            for (String name : names) {
                removed &= removeHeader(name);
            }
        }
        return removed;
    }

    public List<HttpCookie> getCookies() {
        List<String> cookies = headers.get("Cookie");
        if (cookies == null || cookies.isEmpty()) {
            return emptyList();
        }
        String cookie = cookies.get(0);
        if (cookie == null || cookie.trim().isEmpty()) {
            return emptyList();
        }
        List<HttpCookie> list = new ArrayList<>();
        for (String next : cookie.split(";")) {
            list.addAll(HttpCookie.parse(next));
        }
        return list;
    }

    public boolean removeCookie(String name) {
        List<HttpCookie> cookies = getCookies();
        List<HttpCookie> newCookies = new ArrayList<>();
        if (name != null && !name.trim().isEmpty()) {
            for (HttpCookie cookie : cookies) {
                if (!cookie.getName().equals(name)) {
                    newCookies.add(cookie);
                }
            }
        }
        setCookies(newCookies);
        return false;
    }

    public boolean removeCookies(List<String> names) {
        boolean removed = false;
        if (names != null) {
            for (String name : names) {
                removed &= removeCookie(name);
            }
        }
        return removed;
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
