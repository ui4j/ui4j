package com.ui4j.jxbrowser;

import java.util.logging.Level;

import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.spi.BrowserProvider;
import com.ui4j.spi.ShutdownListener;

public class JxBrowserProvider implements BrowserProvider {

	private BrowserEngine engine = new JxEngine();

    public JxBrowserProvider() {
		configureLogging();
    }

	@Override
	public BrowserType getBrowserType() {
		return BrowserType.JxBrowser;
	}

	@Override
	public BrowserEngine create() {
		return engine;
	}

	protected void configureLogging() {
        LoggerProvider.getBrowserLogger().setLevel(Level.SEVERE);
        LoggerProvider.getIPCLogger().setLevel(Level.SEVERE);
        LoggerProvider.getChromiumProcessLogger().setLevel(Level.SEVERE);
	}

	@Override
	public void setShutdownListener(ShutdownListener listener) {
	}
}
