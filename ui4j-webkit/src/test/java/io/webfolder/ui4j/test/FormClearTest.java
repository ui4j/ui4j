package io.webfolder.ui4j.test;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Form;

public class FormClearTest {

    @Test
    public void test1() throws InterruptedException {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(ChildTest.class.getResource("/FormClearTest.html").toExternalForm())) {
            Document document = page.getDocument();
            
            Form form = document.query("#myform").getForm();
            form.clear();

            assertFalse(form.getElement().query("#myinput").getValue() != null);
            assertFalse(form.getElement().query("#myradio").getRadioButton().isChecked());
            assertFalse(form.getElement().query("#mycheckbox").getCheckBox().isChecked());
            assertFalse(form.getElement().query("#mytextarea").getText() != null);
        }
    }

    @Test
    public void test2() throws InterruptedException {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(ChildTest.class.getResource("/FormClearTest.html").toExternalForm())) {
            Document document = page.getDocument();
            
            Form form = document.query("#form").getForm();

            form.getElement().getDocument().query("input[name='a']").setValue("foo");
            form.getElement().getDocument().query("input[name='b']").setValue("bar");

            form.clear();

            assertFalse(form.getElement().getDocument().query("input[name='a']").getValue() != null);
            assertFalse(form.getElement().getDocument().query("input[name='b']").getValue() != null);
        }
    }
}
