package com.ui4j.test;

import org.junit.Assert;
import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;
import com.ui4j.api.interceptor.Response;

public class CookieTest {

	private static Response response;

	@Test
	public void testRequestInterceptor() throws Exception {
		
		BrowserEngine webKit = BrowserFactory.getWebKit();

		PageConfiguration config = new PageConfiguration(new Interceptor() {

			@Override
			public Request beforeLoad(String url) {
				return null;
			}
			
			@Override
			public void afterLoad(Response response) {
				CookieTest.response = response;
			}
		});

		Page page = webKit.navigate("http://httpbin.org/cookies/set?k1=v1&k2=v2", config);

		Assert.assertEquals("v1", response.getCookie("k1").getValue());
		Assert.assertEquals("v2", response.getCookie("k2").getValue());		

		page.close();
	}
}
