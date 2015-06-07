package com.ui4j.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class BingSearch {

    private static class SearchTask implements Callable<List<String>> {

        private BrowserEngine browser;

        private String criteria;

        private List<String> list = new ArrayList<>();

        public SearchTask(BrowserEngine browser, String criteria) {
            this.browser = browser;
            this.criteria = criteria;
        }

        @Override
        public List<String> call() throws Exception {
            // navigate to bing
            Page page = browser.navigate("http://www.bing.com");

            Document doc = page.getDocument();

            // set the searh criteria
            doc.query("#sb_form_q").get().setValue(criteria);

            // click to search button
            doc.query("#sb_form_go").get().click();

            // Normally Ui4j can detect page loads,
            // but for the Ajax requests
            // we cant detect if page is ready or not
            // For such case we should wait manually until page is ready to use
            Thread.sleep(10);

            // list all the results
            List<Element> result = doc.queryAll("h2 > a");
            result.forEach(e -> {
                String title = e.getText().get();
                list.add(title);
                System.out.println(title);
            });

            return list;
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        BrowserEngine webkit = BrowserFactory.getWebKit();

        List<Callable<List<String>>> criterias = new ArrayList<>();
        criterias.add(new SearchTask(webkit, "oracle"));
        criterias.add(new SearchTask(webkit, "javafx"));
        criterias.add(new SearchTask(webkit, "webkit"));
        criterias.add(new SearchTask(webkit, "bbc news"));
        criterias.add(new SearchTask(webkit, "wikipedia"));
        criterias.add(new SearchTask(webkit, "microsoft"));
        criterias.add(new SearchTask(webkit, "cnn news"));
        criterias.add(new SearchTask(webkit, "yahoo"));
        criterias.add(new SearchTask(webkit, "google"));
        criterias.add(new SearchTask(webkit, "c++"));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<List<String>>> result = executor.invokeAll(criterias);
        executor.shutdown();

        webkit.shutdown();
    }
}
