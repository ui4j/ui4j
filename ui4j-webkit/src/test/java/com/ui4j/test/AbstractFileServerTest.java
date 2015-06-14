package com.ui4j.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import fi.iki.elonen.NanoHTTPD;

public abstract class AbstractFileServerTest {

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
}
