package io.webfolder.ui4j.sample;

import java.util.concurrent.CountDownLatch;

import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Element;

public class FormPost {

    private static String result;

    public static void main(String[] args) throws Exception {
        String url = FrameSample.class.getResource("/FormPost.html").toExternalForm();
        Page page = BrowserFactory.getWebKit().navigate(url);

        Element custname = page.getDocument().query("[name='custname']");
        custname.setValue("foo");

        page.getDocument().query("[type='submit']").click();

        CountDownLatch latch = new CountDownLatch(1);

        page.getDocument().query("label").bind("DOMSubtreeModified", e -> {
            result = e.getTarget().getText();
            latch.countDown();
        });

        latch.await();

        System.out.println("Form post result: " + result);

        page.close();

        BrowserFactory.getWebKit().shutdown();
    }
}
