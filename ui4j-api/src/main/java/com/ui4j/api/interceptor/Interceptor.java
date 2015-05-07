package com.ui4j.api.interceptor;

public interface Interceptor {

	void beforeLoad(String url, Request request);

	void afterLoad(String url, Response response);
}
