package com.ui4j.jxbrowser;

import java.util.concurrent.CountDownLatch;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.util.Ui4jException;

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
		BrowserView view = new BrowserView(browser);

		browser.loadURL(url);

		CountDownLatch latch = new CountDownLatch(1);
		JxLoader loader = new JxLoader(latch);
		browser.addLoadListener(loader);
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new Ui4jException(e);
		}

		SelectorEngine se = new JxW3CSelectorEngine(browser);
		JxPage page = new JxPage(view, browser, se);
		return page;
	}

	@Override
	public BrowserType getBrowserType() {
		return BrowserType.JxBrowser;
	}
}
