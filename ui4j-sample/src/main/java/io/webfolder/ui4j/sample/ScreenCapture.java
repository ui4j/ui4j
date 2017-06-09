package io.webfolder.ui4j.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;

public class ScreenCapture {

    public static void main(String[] args) throws FileNotFoundException {
        Page page = BrowserFactory.getWebKit().navigate("https://www.google.com");
        page.show(true);
        page.captureScreen(new FileOutputStream(new File("google.png")));
    }
}