package io.webfolder.ui4j.sample;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;

public class FormSubmit {

	public static void main(String[] args) {
        String url = ReadHtmlFile.class.getResource("/FormSubmit.html").toExternalForm();
        BrowserEngine browser = BrowserFactory.getWebKit();
        Page page = browser.navigate(url);
        page.show();
        Document doc = page.getDocument();
        doc.query("input[type='text']").get().setValue("Foo");
        doc.query("form").get().getForm().get().submit();
        String response = doc.getBody().getText().get();
        System.out.println(response);
	}
}
