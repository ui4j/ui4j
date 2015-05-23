package com.ui4j.jxbrowser;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSObject;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.util.Ui4jException;
import com.ui4j.jxbrowser.js.JsDocument;
import com.ui4j.jxbrowser.proxy.JsProxy;

public class JxEngine implements BrowserEngine {

	private static class JxLoader extends LoadAdapter {

		private CountDownLatch latch;

		public JxLoader(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void onFinishLoadingFrame(FinishLoadingEvent event) {
			if (event.isMainFrame()) {
				latch.countDown();
			}
		}
	};

	public JxEngine() {
        LoggerProvider.getBrowserLogger().setLevel(Level.OFF);
        LoggerProvider.getIPCLogger().setLevel(Level.OFF);
        LoggerProvider.getChromiumProcessLogger().setLevel(Level.OFF);
	}

	@Override
	public void shutdown() {
	}

	@Override
	public Page navigate(String url) {
		return navigate(url, new PageConfiguration());
	}

	@Override
	public Page navigate(String url, PageConfiguration configuration) {
		Browser browser = new Browser();
		browser.loadURL(url);
		CountDownLatch latch = new CountDownLatch(1);
		JxLoader loader = new JxLoader(latch);
		browser.addLoadListener(loader);
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new Ui4jException(e);
		}
		JSObject jsObjectDocument = (JSObject) browser.executeJavaScriptAndReturnValue("document");
		JsDocument jsDocument = new JsProxy<JsDocument>(jsObjectDocument, JsDocument.class).get();
		SelectorEngine se = new JxW3CSelectorEngine(jsDocument);
		JxPage page = new JxPage(browser, se, jsDocument);
		return page;
	}

	@Override
	public BrowserType getBrowserType() {
		return BrowserType.JxBrowser;
	}
}
