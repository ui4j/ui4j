package com.ui4j.sample;

import java.io.IOException;

import netscape.javascript.JSObject;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

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

		page.executeScript("less.render('.class { width: (1 + 1) }', function (e, output) { mycallback.setCss(output.css); });");

		System.out.println(callback.getCss());

		page.close();
	}
}
