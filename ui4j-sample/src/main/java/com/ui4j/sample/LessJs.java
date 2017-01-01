package com.ui4j.sample;

import java.io.IOException;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

import netscape.javascript.JSObject;

public class LessJs {

	public static class MyCallback {

		private String css;

		public void setCss(String css) {
			this.css = css;
		}
		public String getCss() {
			return css;
		}
	}

	public static void main(String[] args) throws IOException {
		BrowserEngine webkit = BrowserFactory.getWebKit();
		String url = ReadHtmlFile.class.getResource("/Less.html").toExternalForm();
		Page page = webkit.navigate(url);
		page.show();
		JSObject obj = (JSObject) page.executeScript("window");

		MyCallback callback = new MyCallback();

		obj.setMember("mycallback", callback);

		// solution 1
		page.executeScript("less.render('.class { width: (1 + 1) }', function (e, output) { mycallback.setCss(output.css); });");

		System.out.println(callback.getCss());

		// solution 2 without custom Java callback
		page.executeScript("var mymap = { }");
		page.executeScript("less.render('.class { width: (2 + 2) }', function (e, output) { mymap.css = output.css; });");

		System.out.println(page.executeScript("mymap.css"));

		page.close();
	}
}
