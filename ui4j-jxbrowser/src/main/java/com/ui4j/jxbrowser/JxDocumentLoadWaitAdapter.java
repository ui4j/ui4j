package com.ui4j.jxbrowser;

import java.util.concurrent.CountDownLatch;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;

public class JxDocumentLoadWaitAdapter extends LoadAdapter {

    private CountDownLatch latch;

    private Browser browser;

    public JxDocumentLoadWaitAdapter(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onFinishLoadingFrame(FinishLoadingEvent event) {
        if (event.isMainFrame()) {
            latch.countDown();
            browser = event.getBrowser();
            event.getBrowser().removeLoadListener(this);
        }
    }

    public Browser getBrowser() {
        return browser;
    }
}