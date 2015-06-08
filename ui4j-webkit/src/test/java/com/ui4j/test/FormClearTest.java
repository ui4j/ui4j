package com.ui4j.test;

import org.junit.Assert;
import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Form;

public class FormClearTest {

    @Test
    public void test() throws InterruptedException {
        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(ChildTest.class.getResource("/FormClearTest.html").toExternalForm())) {
            Document document = page.getDocument();
            
            Form form = document.query("#myform").get().getForm().get();
            form.clear();

            Assert.assertFalse(form.getElement().query("#myinput").get().getValue().isPresent());
            Assert.assertFalse(form.getElement().query("#myradio").get().getRadioButton().get().isChecked());
            Assert.assertFalse(form.getElement().query("#mycheckbox").get().getCheckBox().get().isChecked());
            Assert.assertFalse(form.getElement().query("#mytextarea").get().getText().isPresent());
        }
    }
}
