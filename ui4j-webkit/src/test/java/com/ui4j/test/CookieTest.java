package com.ui4j.test;

import org.junit.Assert;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;
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
	public void testGetCookie() {
		
		BrowserEngine webKit = BrowserFactory.getWebKit();

		PageConfiguration config = new PageConfiguration(new Interceptor() {

			@Override
			public void beforeLoad(String url, Request request) {
			}
			
			@Override
			public void afterLoad(String url, Response response) {
				CookieTest.response = response;
			}
		});

		Page page = webKit.navigate("http://httpbin.org/cookies/set?k1=v1&k2=v2", config);

		Assert.assertEquals("v1", response.getCookie("k1").getValue());
		Assert.assertEquals("v2", response.getCookie("k2").getValue());		

		page.close();
	}

	@Test
	public void setCookieTest() {
		BrowserEngine webKit = BrowserFactory.getWebKit();

		PageConfiguration config = new PageConfiguration(new Interceptor() {

			@Override
			public void beforeLoad(String url, Request request) {
				request.setHeader("Cookie", "Cookie1=Value1; Cookie2=Value2;");
			}

			@Override
			public void afterLoad(String url, Response response) {
				CookieTest.response = response;
			}
		});

		Page page = webKit.navigate("http://httpbin.org/cookies", config);
		String content = page.getWindow().getDocument().getBody().getText();

		JsonObject json = JsonObject.readFrom(content);
		JsonObject cookies = json.get("cookies").asObject();

		String cookie1 = cookies.get("Cookie1").asString();
		String cookie2 = cookies.get("Cookie2").asString();

		Assert.assertEquals("Value1", cookie1);
		Assert.assertEquals("Value2", cookie2);

		page.close();
	}
}
