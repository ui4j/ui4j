package com.ui4j.jxbrowser;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.spi.BrowserProvider;
import com.ui4j.spi.ShutdownListener;

public class JxBrowserProvider implements BrowserProvider, JxBrowserEngine {

	private BrowserEngine engine = new JxEngine();

	@Override
	public BrowserType getBrowserType() {
		return engine.getBrowserType();
	}

	@Override
	public BrowserEngine create() {
		return engine;
	}

	@Override
	public void setShutdownListener(ShutdownListener listener) {
	}
}
