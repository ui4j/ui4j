package io.webfolder.ui4j.test;

import java.util.Locale;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.BrowserType;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;

public class WaitTest {

    private static Document document;

    private static Page page;

    @BeforeClass public static void beforeTest() {
        BrowserEngine browser = BrowserFactory.getBrowser(BrowserType.WebKit);
        page = browser.navigate("https://news.ycombinator.com");
        page.show();
        document = page.getDocument();
    }

    @Test @Ignore public void test() {
        Assert.assertEquals("Hacker News", document.getTitle());
        document.query("a[href='https://github.com/HackerNews/API']").click();
        Assert.assertTrue(document.getTitle().toLowerCase(Locale.ENGLISH).contains("hackernews"));
        page.close();
    }
}
