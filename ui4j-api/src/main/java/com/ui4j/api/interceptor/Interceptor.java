package com.ui4j.api.interceptor;

public interface Interceptor {

	void beforeLoad(Request request);

	void afterLoad(Response response);
}
