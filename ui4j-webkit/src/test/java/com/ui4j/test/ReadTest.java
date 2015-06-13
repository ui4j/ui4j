package com.ui4j.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.dom.Element;
import com.ui4j.api.interceptor.Interceptor;
import com.ui4j.api.interceptor.Request;
import com.ui4j.api.interceptor.Response;

import fi.iki.elonen.NanoHTTPD;

public class ReadTest {

    private static NanoHTTPD httpd;

    @BeforeClass
    public static void before() {
        httpd = new NanoHTTPD(58844) {

            @Override
            public Response serve(IHTTPSession session) {
                URL url = ReadTest.class.getResource(session.getUri());
                if (url == null) {
                    return super.serve(session);
                }
                Path path = new File(url.getFile()).toPath();
                String type = null;
                try {
                    type = Files.probeContentType(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Response response = new Response(type);
                try {
                    response.setData(new FileInputStream(path.toFile()));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return response;
            }
        };
        try {
            httpd.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void after() {
        httpd.stop();
    }

    @Test
    public void test() {
        Page page = BrowserFactory.getWebKit().navigate("http://localhost:58844/ReadTest.html",
                new PageConfiguration(new Interceptor() {

            @Override
            public void beforeLoad(Request request) {
                // no op
            }

            @Override
            public void afterLoad(Response response) {
                // no op
            }
        }));
        List<Element> images = page.getDocument().queryAll("img");

        Element image1 = images.get(0);
        float w1 = image1.getClientWidth();
        float h1 = image1.getClientHeight();
        assertEquals(w1, 43.0f, 0);
        assertEquals(h1, 25.0f, 0);

        Element image2 = images.get(1);
        float w2 = image2.getClientWidth();
        float h2 = image2.getClientHeight();
        assertEquals(w2, 43.0f, 0);
        assertEquals(h2, 25.0f, 0);
    }
}
