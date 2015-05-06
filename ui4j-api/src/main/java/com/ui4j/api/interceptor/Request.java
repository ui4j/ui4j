package com.ui4j.api.interceptor;

import java.util.HashMap;
import java.util.Map;

public class Request {

	private String uri;

	private Map<String, String> headers = new HashMap<>();

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getUri() {
		return uri;
	}

	public Request setHeader(String name, String value) {
		headers.put(name, value);
		return this;
	}

	@Override
	public String toString() {
		return "Request [uri=" + uri + ", headers=" + headers + "]";
	}
}
