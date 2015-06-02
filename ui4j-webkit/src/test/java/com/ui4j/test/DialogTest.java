package com.ui4j.test;

import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dialog.AlertHandler;
import com.ui4j.api.dialog.ConfirmHandler;
import com.ui4j.api.dialog.DialogEvent;
import com.ui4j.api.dialog.PromptDialogEvent;
import com.ui4j.api.dialog.PromptHandler;

public class DialogTest {

    private static Page page;

    private static String alertMessage;
    
    private static String promptMessage;

    @BeforeClass public static void beforeTest() {
        String url = ElementTest.class.getResource("/TestPage.html").toExternalForm();
        BrowserEngine browser = BrowserFactory.getBrowser(BrowserType.WebKit);
        page = browser.navigate(url);
    }

    @Test public void testAlertDialog() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        page.setAlertHandler(new AlertHandler() {

            @Override
            public void handle(DialogEvent event) {
                alertMessage = event.getMessage();
                latch.countDown();
            }
        });
        page.executeScript("alert('foo')");
        latch.await();
        Assert.assertEquals("foo", alertMessage);
    }

    @Test public void testPromptDialog() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        page.setPromptHandler(new PromptHandler() {

            @Override
            public String handle(PromptDialogEvent event) {
                promptMessage = event.getMessage();
                latch.countDown();
                return null;
            }
        });
        page.executeScript("prompt('bar')");
        latch.await();
        Assert.assertEquals("bar", promptMessage);
    }

    @Test public void testConfirmDialog() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        page.setConfirmHandler(new ConfirmHandler() {

            @Override
            public boolean handle(DialogEvent event) {
                latch.countDown();
                return true;
            }
        });
        Object executeScript = page.executeScript("confirm('bar')");
        latch.await();
        boolean ret = Boolean.parseBoolean(executeScript.toString());
        Assert.assertTrue(ret);
    }
}
