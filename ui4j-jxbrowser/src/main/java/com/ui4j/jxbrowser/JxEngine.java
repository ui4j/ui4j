package com.ui4j.jxbrowser;

import static com.teamdev.jxbrowser.chromium.LoggerProvider.getBrowserLogger;
import static com.teamdev.jxbrowser.chromium.LoggerProvider.getChromiumProcessLogger;
import static com.teamdev.jxbrowser.chromium.LoggerProvider.getIPCLogger;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.SEVERE;

import java.util.concurrent.CountDownLatch;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.browser.SelectorEngine;
import com.ui4j.api.util.Ui4jException;

public class JxEngine implements BrowserEngine {

	private static class JxLoadAdapter extends LoadAdapter {

		private CountDownLatch latch;

		public JxLoadAdapter(CountDownLatch latch) {
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
        getBrowserLogger().setLevel(SEVERE);
        getIPCLogger().setLevel(SEVERE);
        getChromiumProcessLogger().setLevel(SEVERE);
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
		JxLoadAdapter jxLoadAdapter = new JxLoadAdapter(latch);
		browser.addLoadListener(jxLoadAdapter);
		try {
			latch.await(60, SECONDS);
		} catch (InterruptedException e) {
			throw new Ui4jException(e);
		}
		browser.removeLoadListener(jxLoadAdapter);
		SelectorEngine selectorEngine = new JxW3CSelectorEngine(browser);
		JxPage page = new JxPage(browser, selectorEngine);
		return page;
	}

	@Override
	public BrowserType getBrowserType() {
		return BrowserType.JxBrowser;
	}
}
