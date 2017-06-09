package io.webfolder.ui4j.test;

import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.BrowserType;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.browser.PageConfiguration;
import io.webfolder.ui4j.api.dialog.AlertHandler;
import io.webfolder.ui4j.api.dialog.ConfirmHandler;
import io.webfolder.ui4j.api.dialog.DialogEvent;
import io.webfolder.ui4j.api.dialog.PromptDialogEvent;
import io.webfolder.ui4j.api.dialog.PromptHandler;

public class DialogTest {

    private static Page page;

    private static String alertMessage;
    
    private static String promptMessage;

    private static CountDownLatch alertLatch = new CountDownLatch(1);

    private static CountDownLatch promptLatch = new CountDownLatch(1);

    private static CountDownLatch confirmLatch = new CountDownLatch(1);

    @BeforeClass public static void beforeTest() {
        String url = ElementTest.class.getResource("/TestPage.html").toExternalForm();
        BrowserEngine browser = BrowserFactory.getBrowser(BrowserType.WebKit);
        page = browser.navigate(url, new PageConfiguration()
            .setAlertHandler(new AlertHandler() {

            @Override
            public void handle(DialogEvent event) {
                alertMessage = event.getMessage();
                alertLatch.countDown();
            }
        }).setPromptHandler(new PromptHandler() {

            @Override
            public String handle(PromptDialogEvent event) {
                promptMessage = event.getMessage();
                promptLatch.countDown();
                return null;
            }
        }).setConfirmHandler(new ConfirmHandler() {

            @Override
            public boolean handle(DialogEvent event) {
                confirmLatch.countDown();
                return true;
            }
        }));
    }

    @Test public void testAlertDialog() throws InterruptedException {
        page.executeScript("alert('foo')");
        alertLatch.await();
        Assert.assertEquals("foo", alertMessage);
    }

    @Test public void testPromptDialog() throws InterruptedException {
        page.executeScript("prompt('bar')");
        promptLatch.await();
        Assert.assertEquals("bar", promptMessage);
    }

    @Test public void testConfirmDialog() throws InterruptedException {
        Object executeScript = page.executeScript("confirm('bar')");
        confirmLatch.await();
        boolean ret = Boolean.parseBoolean(executeScript.toString());
        Assert.assertTrue(ret);
    }
}
