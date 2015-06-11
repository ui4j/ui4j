package com.ui4j.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.CheckBox;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;
import com.ui4j.api.dom.Input;
import com.ui4j.api.dom.Option;
import com.ui4j.api.dom.Select;
import com.ui4j.api.event.DomEvent;
import com.ui4j.api.event.EventHandler;
import com.ui4j.api.util.Point;
import com.ui4j.api.util.Ui4jException;
import com.ui4j.spi.PageView;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElementTest {

    private static Document document;

    @BeforeClass public static void beforeTest() {
        String url = ElementTest.class.getResource("/TestPage.html").toExternalForm();
        BrowserEngine browser = BrowserFactory.getBrowser(BrowserType.WebKit);
        Page page = browser.navigate(url);
        page.show();
        document = page.getDocument();
    }

    @Test public void t01_append() {
        document.getBody().append("<div>sample div</div>");
    }

    @Test public void t02_query() {
        Assert.assertEquals("<div>sample div</div>", document.getBody().query("div").get().getOuterHTML());
    }

    @Test public void t03_removeDiv() {
        document.getBody().query("div").get().remove();
    }

    @Test public void t04_attr() {
        document.getBody().setAttribute("foo", "bar");
        Assert.assertEquals("bar", document.getBody().getAttribute("foo").get());
        document.getBody().removeAttribute("foo");
        Assert.assertFalse(document.getBody().getAttribute("foo").isPresent());
        Assert.assertFalse(document.getBody().hasAttribute("foo"));
        Map<String, String> attributes = new HashMap<>();
        attributes.put("foo", "bar");
        attributes.put("bar", "foo");
        document.getBody().setAttribute(attributes);
        Assert.assertTrue(document.getBody().hasAttribute("foo"));
        Assert.assertTrue(document.getBody().hasAttribute("bar"));
        document.getBody().removeAttribute("foo");
        document.getBody().removeAttribute("bar");
    }

    @Test public void t05_class() {
        Element label = document.createElement("label");
        label.setText("my label");
        document.getBody().append(label);
        label.addClass("bold-text");
        Assert.assertTrue(label.hasClass("bold-text"));
        List<String> classes = label.getClasses();
        Assert.assertEquals(1, classes.size());
        Assert.assertEquals("bold-text", classes.get(0));
        label.removeClass("bold-text");
        Assert.assertFalse(label.hasClass("bold-text"));
        label.toggleClass("bold-text");
        Assert.assertTrue(label.hasClass("bold-text"));
        label.toggleClass("bold-text");
        Assert.assertFalse(label.hasClass("bold-text"));
        label.remove();
    }

    @Test public void t06_text() {
        Element div = document.createElement("div");
        Assert.assertFalse(div.getText().isPresent());
        div.setText("foo bar");
        Assert.assertEquals("foo bar", div.getText().get());
        div.remove();
    }

    @Test public void t06_tagName() {
        Element div = document.createElement("div");
        Assert.assertEquals("div", div.getTagName());
    }

    @Test public void t07_value() {
        Element input = document.createElement("input");
        document.getBody().append(input);
        Assert.assertFalse(input.getValue().isPresent());
        input.setValue("foo bar");
        input.remove();
    }

    @Test public void t08_bind() {
        Element button = document.createElement("input");
        document.getBody().append(button);
        button.setAttribute("type", "button");
        button.setValue("click!");
        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder flag = new StringBuilder();
        button.bindClick(new EventHandler() {

            @Override
            public void handle(DomEvent event) {
                flag.append("clicked only once");
                Assert.assertEquals("click", event.getType());
                latch.countDown();
            }
        });
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new Ui4jException(e);
        }
        button.click();
        Assert.assertEquals("clicked only once", flag.toString());
        button.unbind("click");
        button.click();
        Assert.assertEquals("clicked only once", flag.toString());
        button.remove();
    }

    @Test public void t09_id() {
        Element div = document.createElement("div");
        Assert.assertFalse(div.getId().isPresent());
        div.setId("foo");
        Assert.assertEquals("foo", div.getId().get());
        div.remove();
    }

    @Test public void t10_data() {
        Element div = document.createElement("div");
        document.getBody().append(div);
        Assert.assertNull(div.getProperty("foo"));
        div.setProperty("foo", Collections.emptyList());
        Assert.assertEquals(Collections.emptyList(), div.getProperty("foo"));
        div.removeProperty("foo");
        Assert.assertNull(div.getProperty("foo"));
        div.remove();
    }

    @Test public void t11_after() {
        Element div = document.createElement("div");
        document.getBody().append(div);
        div.after("<label>foo</label>");
        Assert.assertEquals("<div></div>", document.query("label").get().getPrev().get().getOuterHTML());
        document.getBody().empty();
        div = document.createElement("div");
        document.getBody().append(div);
        div.after(document.createElement("input"));
        Assert.assertEquals("<input>", document.query("div").get().getNext().get().getOuterHTML());
        document.getBody().empty();
    }

    @Test public void t12_before() {
        Element div = document.createElement("div");
        document.getBody().append(div);
        div.before("<label>foo</label>");
        Assert.assertEquals("<div></div>", document.query("label").get().getNext().get().getOuterHTML());
        document.getBody().empty();
        div = document.createElement("div");
        document.getBody().append(div);
        div.before(document.createElement("input"));
        Assert.assertEquals("<div></div>", document.query("input").get().getNext().get().getOuterHTML());
        document.getBody().empty();
    }

    @Test public void t13_isHtml() {
        Element label = document.createElement("label");
        Assert.assertTrue(label.isHtmlElement());
        label.remove();
        List<Element> txt = document.parseHTML("foo");
        Assert.assertFalse(txt.get(0).isHtmlElement());
        txt.get(0).remove();
    }

    @Test public void t14_innerHTML() {
        Element label = document.createElement("label");
        Assert.assertEquals("", label.getInnerHTML());
        label.setInnerHTML("<div>foo</div>");
        Assert.assertEquals("<div>foo</div>", label.getInnerHTML());
        label.remove();
    }

    @Test public void t14_focus() {
        Element input = document.createElement("input");
        document.getBody().append(input);
        CountDownLatch latch = new CountDownLatch(1);
        input.bind("focus", new EventHandler() {
            
            @Override
            public void handle(DomEvent event) {
                Assert.assertEquals("focus", event.getType());
                latch.countDown();
            }
        });
        try {
            latch.await(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new Ui4jException(e);
        }
        input.focus();
        input.remove();
    }

    @Test public void t15_query() {
        Element div = document.parseHTML("<div><label>foo-1</label><label>foo-2</label></div>").get(0);
        document.getBody().append(div);
        div = document.getBody().query("div").get();
        Assert.assertEquals("<label>foo-1</label>", div.query("label").get().getOuterHTML());
        Assert.assertEquals("<label>foo-2</label>", div.queryAll("label").get(1).getOuterHTML());
        div.remove();
    }

    @Test public void t16_contains() {
        Element div = document.parseHTML("<div><label>foo-1</label><label>foo-2</label></div>").get(0);
        document.getBody().append(div);
        div = document.query("div").get();
        Assert.assertTrue(div.contains(document.query("label").get()));
    }

    @Test public void t17_detach() {
        List<Element> elements = document.parseHTML("<div><div>foo</div></div>");
        Element div = elements.get(0);
        Assert.assertFalse(div.isAttached());
        Assert.assertEquals("<div>foo</div>", div.query("div").get().getOuterHTML());
        document.getBody().append(div);
        Assert.assertTrue(div.isAttached());
        div.remove();
    }

    @Test public void t18_on() {
        Element div = document.parseHTML("<div><input type='button' value='click' /></div>").get(0);
        document.getBody().append(div);
        Element input = div.find("input").get(0);
        StringBuilder builder = new StringBuilder();
        div.on("click", "input", new EventHandler() {

            @Override
            public void handle(DomEvent event) {
                Assert.assertEquals("click", event.getType());
                Assert.assertTrue(event.getTarget().is("input"));
                builder.append("clicked only once");
            }
        });
        input.click();
        div.off();
        input.click();
        Assert.assertEquals("clicked only once", builder.toString());
    }

    @Test public void t19_browserType() {
        BrowserEngine webKit = BrowserFactory.getWebKit();
        Assert.assertEquals(BrowserType.WebKit, webKit.getBrowserType());
        Page page = webKit.navigate("about:blank");
        page.hide();
        page.show();
        Assert.assertNotNull(page.getWindow());
        Assert.assertNotNull(page.getEngine());
        PageView pv = (PageView) page;
        Assert.assertNotNull(pv.getView());
        page.executeScript("var s = '20';");
        Assert.assertEquals(BrowserType.WebKit, page.getBrowserType());
        page.close();
    }

    @Test public void t20_parent() {
        Element div = document.parseHTML("<div><span>foo</span></div>").get(0);
        Assert.assertEquals("div", div.query("span").get().getParent().get().getTagName());
    }

    @Test public void t21_input() {
        Element element = document.parseHTML("<input type='text' value='foo' />").get(0);
        Input input = element.getInput().get();
        Assert.assertNotNull(input);
        Assert.assertFalse(input.isDisabled());
        input.setDisabled(true);
        Assert.assertTrue(input.isDisabled());
        Assert.assertFalse(input.isReadOnly());
        input.setReadOnly(true);
        Assert.assertTrue(input.isReadOnly());
        Assert.assertNotNull(input.getElement());
        Assert.assertFalse(input.isHidden());
    }

    @Test public void t22_checkbox() {
        Element element = document.parseHTML("<input type='checkbox' />").get(0);
        CheckBox checkBox = element.getCheckBox().get();
        Assert.assertNotNull(checkBox);
        Assert.assertFalse(checkBox.isChecked());
        checkBox.setChecked(true);
        Assert.assertTrue(checkBox.isChecked());
        Assert.assertNotNull(checkBox.getElement());
    }

    @Test public void t23_select() {
        Element element = document.parseHTML("<select><option value='foo1'>foo</option><option value='bar1'>bar</option></select>").get(0);
        Select select = element.getSelect().get();
        Assert.assertNotNull(select);
        List<Option> options = select.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(0, select.getSelectedIndex());
        select.setSelectedIndex(1);
        Assert.assertEquals(1, select.getSelectedIndex());
        select.clearSelection();
        Assert.assertEquals(-1, select.getSelectedIndex());
        select.change();
        Option option = select.getOption(0);
        Assert.assertNotNull(option);
        Assert.assertEquals(2, select.getOptions().size());
        Assert.assertFalse(select.isDisabled());
        select.setDisabled(true);
        Assert.assertTrue(select.isDisabled());
    }

    @Test public void t24_option() {
        Element element = document.parseHTML("<select><option value='foo1'>foo</option><option value='bar1'>bar</option></select>").get(0);
        Option option = element.query("option").get().getOption().get();
        Assert.assertNotNull(option);
        Assert.assertEquals("foo", option.getElement().getText().get());
        Assert.assertEquals("foo1", option.getElement().getValue().get());
        Assert.assertNotNull(option.getElement());
        Assert.assertNotNull(option.getElement());
        Assert.assertTrue(option.isSelected());
        option.getElement().setText("foofoo");
        Assert.assertEquals("foofoo", option.getElement().getText().get());
        option.getElement().setValue("foo2");
        Assert.assertEquals("foo2", option.getElement().getValue().get());
    }


    @Test public void t25_prepend() {
        Element element = document.parseHTML("<div>foo</div>").get(0);
        element.prepend("<span>bar</span>");
        Assert.assertEquals("<div><span>bar</span>foo</div>", element.getOuterHTML());
        Element bar = document.parseHTML("<span>bar</span>").get(0);
        element.prepend(bar);
        Assert.assertEquals("<div><span>bar</span><span>bar</span>foo</div>", element.getOuterHTML());
    }

    @Test public void t26_offset() {
        Element element = document.parseHTML("<div style='position: absolute; left: 20px; top: 20px'>foo</div>").get(0);
        document.getBody().append(element);
        Point p = new Point(20, 20);
        Assert.assertEquals(p, element.getOffset());
    }

    @Test public void t27_scrollIntoView() {
        Element element = document.parseHTML("<div style='position: absolute; left: 20px; top: 20px'>foo</div>").get(0);
        document.getBody().append(element);
        element.scrollIntoView(true);
    }

    @Test public void t28_css() {
        Element element = document.parseHTML("<div>foo</div>").get(0);
        element.setCss("color", "red");
        String color = element.getCss("color").get();
        Assert.assertEquals("red", color);
        Map<String, String> map = new HashMap<>();
        map.put("font-family", "Verdana");
        map.put("font-size", "22px");
        element.setCss(map);
        Assert.assertEquals("Verdana", element.getCss("font-family").get());
        Assert.assertEquals("22px", element.getCss("font-size").get());
        element.setCss("background-color", "white", "!important");
        Assert.assertEquals("color: red; font-size: 22px; font-family: Verdana; background-color: white !important;", element.getAttribute("style").get());
        element.removeCss("font-family");
        Assert.assertFalse(element.getCss("font-family").isPresent());
        element.remove();
    }

    @Test public void t29_title() {
        Element element = document.parseHTML("<div>foo</div>").get(0);
        document.getBody().append(element);
        Assert.assertFalse(element.getTitle().isPresent());
        element.setTitle("my title");
        Assert.assertEquals("my title", element.getTitle().get());
    }

    @Test public void t30_tabIndex() {
        Element element = document.parseHTML("<input />").get(0);
        Assert.assertEquals(0, element.getTabIndex());
        element.setTabIndex(1);
        Assert.assertEquals(1, element.getTabIndex());
    }

    @Test public void t31_equalNode() {
        Element input1 = document.parseHTML("<input />").get(0);
        Element input2 = document.parseHTML("<input />").get(0);
        Assert.assertTrue(input1.isEqualNode(input2));
        Assert.assertTrue(input1.isEqualNode(input1));
        Assert.assertTrue(input2.isEqualNode(input2));
        Assert.assertTrue(input2.isEqualNode(input1));
    }

    @Test public void t32_sameNode() {
        Element input1 = document.parseHTML("<input />").get(0);
        Element input2 = document.parseHTML("<input />").get(0);
        Assert.assertFalse(input1.isSameNode(input2));
        Assert.assertTrue(input1.isSameNode(input1));
        Assert.assertTrue(input2.isSameNode(input2));
        Assert.assertFalse(input2.isSameNode(input1));
    }

    @Test public void t33_height() {
        Element div = document.parseHTML("<div style='height: 20px; width: 20px'>foo</div>").get(0);
        document.getBody().append(div);
        Assert.assertEquals(20, div.getOuterHeight(), 0);
        Assert.assertEquals(20, div.getOuterWidth(), 0);
        Assert.assertEquals(20, div.getClientHeight(), 0);
        Assert.assertEquals(20, div.getClientWidth(), 0);
    }

    @Test public void t34_appendTo() {
        Element div = document.parseHTML("<div>foo</div>").get(0);
        document.getBody().append(div);
        Element span = document.parseHTML("<span>bar</span>").get(0);
        document.getBody().append(span);
        div.appendTo(span);
        Assert.assertEquals("<span>bar<div>foo</div></span>", span.getOuterHTML());
    }

    @Test public void t35_hide() {
        Element div = document.parseHTML("<div>foo</div>").get(0);
        div.hide();
        Assert.assertEquals("none", div.getCss("display").get());
        div.show();
        Assert.assertFalse(div.getCss("display").isPresent());
    }

    @Test public void t36_clone() {
        Element div = document.parseHTML("<div>foo</div>").get(0);
        document.getBody().append(div);
        Element clone = div.cloneElement();
        Assert.assertTrue(div.isEqualNode(clone));
        Assert.assertFalse(div.isSameNode(clone));
        div.remove();
    }

    @Test public void t37_offsetParent() {
        Element div = document.parseHTML("<div>foo<span id='bar'>bar</span></div>").get(0);
        document.getBody().append(div);
        div.getOffsetParent();
        div.remove();
    }

    @Test public void t38_possition() {
        Element div = document.parseHTML("<div style=\"position: absolute; top: 20px; left: 20px\">foo</div>").get(0);
        document.getBody().append(div);
        Assert.assertEquals(new Point(20, 20), div.getPosition().get());
        div.remove();
    }

    @Test public void t39_replaceWith() {
        Element div = document.parseHTML("<div id='barfoo'><span id='foobar'>my text</span></div>").get(0);
        document.getBody().append(div);
        document.query("#foobar").get().replaceWith("my text content");
        Assert.assertEquals("my text content", document.query("#barfoo").get().getInnerHTML());
        document.getBody().append(document.parseHTML("<div id='foofoo'>my div</div>").get(0));
        document.query("#barfoo").get().replaceWith(document.query("#foofoo").get());
        Assert.assertFalse(document.getBody().query("#barfoo").isPresent());
        Assert.assertEquals("my div", document.query("#foofoo").get().getInnerHTML());
    }

    @Test public void t40_emptyElement() {
        Optional<Element> div = document.getBody().query("#invalid-id");
        Assert.assertNotNull(div);
        Assert.assertFalse(div.isPresent());
    }

    @Test public void t41_closest() {
        Element div = document.parseHTML("<div><label>Name</label><div><input id='txtName' /><span><input id='txtSurname' /></span></div></div>").get(0);
        Assert.assertEquals("txtName", div.closest("input").get().getId().get());
    }

    @Test public void t42_removeBody() {
        document.getBody().setAttribute("foo", "bar");
        document.getBody().remove();
        Assert.assertEquals("bar", document.getBody().getAttribute("foo").get());
    }
}
