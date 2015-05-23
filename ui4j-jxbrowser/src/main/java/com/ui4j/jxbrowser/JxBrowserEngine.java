
package com.ui4j.jxbrowser;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;

public interface JxBrowserEngine extends BrowserEngine {

	BrowserEngine engine = new JxEngine();

	default void shutdown() {
		engine.shutdown();
	}

    default Page navigate(String url) {
    	return engine.navigate(url);
    }

    default Page navigate(String url, PageConfiguration configuration) {
    	return engine.navigate(url, configuration);
    }

    default BrowserType getBrowserType() {
    	return engine.getBrowserType();
    }
}
