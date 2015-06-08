package com.ui4j.test;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Form;

public class FormClearTest {

    @Test
    public void test1() throws InterruptedException {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(ChildTest.class.getResource("/FormClearTest.html").toExternalForm())) {
            Document document = page.getDocument();
            
            Form form = document.query("#myform").get().getForm().get();
            form.clear();

            assertFalse(form.getElement().query("#myinput").get().getValue().isPresent());
            assertFalse(form.getElement().query("#myradio").get().getRadioButton().get().isChecked());
            assertFalse(form.getElement().query("#mycheckbox").get().getCheckBox().get().isChecked());
            assertFalse(form.getElement().query("#mytextarea").get().getText().isPresent());
        }
    }

    @Test
    public void test2() throws InterruptedException {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(ChildTest.class.getResource("/FormClearTest.html").toExternalForm())) {
            Document document = page.getDocument();
            
            Form form = document.query("#form").get().getForm().get();

            form.getElement().getDocument().query("input[name='a']").get().setValue("foo");
            form.getElement().getDocument().query("input[name='b']").get().setValue("bar");

            form.clear();

            assertFalse(form.getElement().getDocument().query("input[name='a']").get().getValue().isPresent());
            assertFalse(form.getElement().getDocument().query("input[name='b']").get().getValue().isPresent());
        }
    }
}
