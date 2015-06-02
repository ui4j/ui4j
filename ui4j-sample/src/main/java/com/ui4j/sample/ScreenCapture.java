package com.ui4j.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

public class ScreenCapture {

    public static void main(String[] args) throws FileNotFoundException {
        Page page = BrowserFactory.getWebKit().navigate("https://www.google.com");
        page.show(true);
        page.captureScreen(new FileOutputStream(new File("google.png")));
    }
}