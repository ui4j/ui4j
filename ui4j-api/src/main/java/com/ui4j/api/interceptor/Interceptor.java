package com.ui4j.api.interceptor;

public interface Interceptor {

	Request beforeLoad(String url);

	void afterLoad(Response response);
}
