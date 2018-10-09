package io.webfolder.ui4j.webkit.dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLFrameElement;
import org.w3c.dom.html.HTMLIFrameElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

import com.sun.webkit.dom.DocumentImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLFrameElementImpl;
import com.sun.webkit.dom.HTMLIFrameElementImpl;
import com.sun.webkit.dom.NodeImpl;

import io.webfolder.ui4j.api.browser.SelectorType;
import io.webfolder.ui4j.api.dom.CheckBox;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Element;
import io.webfolder.ui4j.api.dom.EventTarget;
import io.webfolder.ui4j.api.dom.Form;
import io.webfolder.ui4j.api.dom.Input;
import io.webfolder.ui4j.api.dom.Option;
import io.webfolder.ui4j.api.dom.RadioButton;
import io.webfolder.ui4j.api.dom.Select;
import io.webfolder.ui4j.api.event.EventHandler;
import io.webfolder.ui4j.api.util.Point;
import io.webfolder.ui4j.spi.DelegatingEventHandler;
import io.webfolder.ui4j.spi.JavaScriptEngine;
import io.webfolder.ui4j.spi.NodeUnbindVisitor;
import io.webfolder.ui4j.spi.PageContext;
import io.webfolder.ui4j.webkit.WebKitMapper;
import io.webfolder.ui4j.webkit.browser.WebKitPageContext;
import io.webfolder.ui4j.webkit.spi.WebKitJavaScriptEngine;
import netscape.javascript.JSObject;

public class WebKitElement implements Element, EventTarget {

    protected Node element;

    protected Document document;

    protected PageContext context;

    protected WebKitJavaScriptEngine engine;

    public WebKitElement(Node element, Document document, PageContext context, WebKitJavaScriptEngine engine) {
        this.element = element;
        this.document = document;
        this.context = context;
        this.engine = engine;
    }

    @Override
    public String getAttribute(String name) {
        String val = getHtmlElement().getAttribute(name);
        if (val == null || val.isEmpty()) {
            return null;
        } else {
            return val;
        }
    }

    @Override
    public Element setAttribute(String name, String value) {
        getHtmlElement().setAttribute(name, value);
        return this;
    }

    @Override
    public Element setAttribute(Map<String, String> attributes) {
        if (attributes == null) {
            return this;
        }
        for (Map.Entry<String, String> next : attributes.entrySet()) {
            setAttribute(next.getKey(), next.getValue());
        }
        return this;
    }

    @Override
    public Element removeAttribute(String name) {
        getHtmlElement().removeAttribute(name);
        return this;
    }

    @Override
    public boolean hasAttribute(String name) {
        return getHtmlElement().hasAttribute(name);
    }

    @Override
    public Element addClass(String... names) {
        if (names != null && names.length > 0) {
            for (String name : names) {
                JSObject classList = (JSObject) getProperty("classList");
                classList.call("add", name);
            }
        }
        return this;
    }

    @Override
    public Element removeClass(String... names) {
        if (names != null && names.length > 0) {
            for (String name : names) {
                JSObject classList = (JSObject) getProperty("classList");
                classList.call("remove", name);
            }
        }
        return this;
    }

    @Override
    public Element toggleClass(String name) {
        JSObject classList = (JSObject) getProperty("classList");
        classList.call("toggle", name);
        return this;
    }

    @Override
    public boolean hasClass(String name) {
        JSObject classList = (JSObject) getProperty("classList");
        Object result = classList.call("contains", name);
        return Boolean.parseBoolean(result.toString());
    }

    @Override
    public List<String> getClasses() {
        HTMLElementImpl elementImpl = getHtmlElement();
        JSObject classList = (JSObject) elementImpl.getMember("classList");
        int length = (int) classList.getMember("length");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            String className = classList.call("item", i).toString();
            list.add(className);
        }
        return list;
    }

    @Override
    public String getText() {
        String textContent = element.getTextContent();
        if (textContent == null || textContent.isEmpty()) {
            return null;
        }
        return textContent;
    }

    @Override
    public Element setText(String text) {
        HTMLElementImpl elementImpl = getHtmlElement();
        elementImpl.setTextContent(text);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTagName() {
        return getNode().getNodeName().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String getValue() {
        String value = null;
        if (element instanceof HTMLInputElement) {
            value = ((HTMLInputElement) element).getValue();
        } else if (element instanceof HTMLOptionElement) {
            value = ((HTMLOptionElement) element).getValue();
        }
        return value == null || value.isEmpty() ? null : value;
    }

    @Override
    public Element setValue(String value) {
        if (element instanceof HTMLInputElement) {
            ((HTMLInputElement) element).setValue(value);
        } else if (element instanceof HTMLOptionElement) {
            ((HTMLOptionElement) element).setValue(value);
        }
        return this;
    }

    @Override
    public Element bind(String event, EventHandler handler) {
        context.getEventManager().bind(this, event, handler);
        return this;
    }

    @Override
    public Element bindClick(EventHandler handler) {
        bind("click", handler);
        return this;
    }

    @Override
    public Element unbind(String event) {
        context.getEventManager().unbind(this, event);
        return this;
    }

    @Override
    public Element unbind(EventHandler handler) {
        context.getEventManager().unbind(this, handler);
        return this;
    }

    @Override
    public List<Element> getChildren() {
        NodeList nodes = element.getChildNodes();
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = ((WebKitPageContext) context).createElement(node, document, engine);
                elements.add(element);
            }
        }
        return elements;
    }

    @Override
    public List<Element> find(String selector) {
        List<Element> elements = new ArrayList<>();
        find(getHtmlElement(), elements, selector);
        if (elements.isEmpty()) {
            return Collections.emptyList();
        }
        return elements;
    }

    private void find(Node node, List<Element> elements, String selector) {
        if (Node.ELEMENT_NODE != node.getNodeType()) {
            return;
        }
        NodeList nodes = node.getChildNodes();
        int length = nodes.getLength();
        if (length <= 0) {
            return;
        }
        for (int i = 0; i < length; i++) {
            Node item = nodes.item(i);
            if (Node.ELEMENT_NODE == item.getNodeType()) {
                Element element = ((WebKitPageContext) context).createElement(item, document, engine);
                if (element.is(selector)) {
                    elements.add(element);
                }
                find(item, elements, selector);
            }
        }
    }

    @Override
    public Element unbind() {
        context.getEventManager().unbind(this);
        return this;
    }

    @Override
    public Element empty() {
        HTMLElementImpl htmlElementImpl = getHtmlElement();
        Node child = htmlElementImpl.getLastChild();
        while (child != null) {
            new NodeUnbindVisitor(context, this).walk();
            htmlElementImpl.removeChild(child);
            child = htmlElementImpl.getLastChild();
        }
        return this;
    }

    @Override
    public void remove() {
        if (isHtmlElement() && !getTagName().equals("body")) {
            unbind();
            new NodeUnbindVisitor(context, this).walk();
            if (isAttached()) {
                HTMLElementImpl elementImpl = getHtmlElement();
                elementImpl.getParentElement().removeChild(elementImpl);
            }
        }
    }

    @Override
    public Element click() {
        HTMLElementImpl element = getHtmlElement();
        element.click();
        return this;
    }

    @Override
    public Element getParent() {
        Node parentNode = element.getParentNode();
        if (parentNode == null) {
            return null;
        }
        return ((WebKitPageContext) context).createElement(element.getParentNode(), document, engine);
    }

    @Override
    public Input getInput() {
        return new Input(this);
    }

    @Override
    public CheckBox getCheckBox() {
        return new CheckBox(this);
    }

    @Override
    public RadioButton getRadioButton() {
        return new RadioButton(this);
    }

    @Override
    public Option getOption() {
        if (element instanceof HTMLOptionElement) {
            return new Option(this);
        } else {
            return null;
        }
    }

    @Override
    public Select getSelect() {
        if (element instanceof HTMLSelectElement) {
            return new Select(this);
        } else {
            return null;
        }
    }

    @Override
    public Form getForm() {
        if (element instanceof HTMLFormElement) {
            return new Form(this);
        }
        return null;
    }

    @Override
    public String getId() {
        String id = getHtmlElement().getId();
        if (id == null || id.isEmpty()) {
            return null;
        }
        return id;
    }

    @Override
    public Element setId(String id) {
        getHtmlElement().setId(id);
        return this;
    }

    @Override
    public void setProperty(String key, Object value) {
        JSObject obj = (JSObject) getHtmlElement();
        obj.setMember(key, value);
    }

    @Override
    public void removeProperty(String key) {
        JSObject obj = (JSObject) getHtmlElement();
        obj.removeMember(key);
    }

    @Override
    public Object getProperty(String key) {
        JSObject obj = (JSObject) getHtmlElement();
        Object member = obj.getMember(key);
        if (member instanceof String && "undefined".equals(member)) {
            return null;
        }
        return member;
    }

    @Override
    public Element append(String html) {
        List<Element> elements = document.parseHTML(html);
        for (Element next : elements) {
            append(next);
        }
        return this;
    }

    @Override
    public Element append(Element element) {
        if (element instanceof WebKitElement) {
            WebKitElement elementImpl = (WebKitElement) element;
            NodeImpl nodeImpl = elementImpl.getNode();
            getHtmlElement().appendChild(nodeImpl);
        }
        return this;
    }

    @Override
    public Element after(String html) {
        List<Element> list = document.parseHTML(html);
        for (Element element : list) {
            WebKitElement elementImpl = (WebKitElement) element;
            NodeImpl node = elementImpl.getNode();
            HTMLElementImpl htmlElementImpl = getHtmlElement();
            htmlElementImpl.getParentNode().insertBefore(node, htmlElementImpl.getNextElementSibling());
        }
        return this;
    }

    @Override
    public Element after(Element element) {
        if (element instanceof WebKitElement) {
            HTMLElementImpl htmlElementImpl = this.getHtmlElement();
            htmlElementImpl.getParentNode().insertBefore(((WebKitElement) element).getNode(), htmlElementImpl.getNextElementSibling());
        }
        return this;
    }

    @Override
    public Element before(String html) {
        List<Element> list = document.parseHTML(html);
        for (Element element : list) {
            WebKitElement elementImpl = (WebKitElement) element;
            HTMLElementImpl htmlElementImpl = getHtmlElement();
            htmlElementImpl.getParentNode().insertBefore(elementImpl.getNode(), htmlElementImpl);
        }
        return this;
    }

    @Override
    public Element before(Element element) {
        if (element instanceof WebKitElement) {
            HTMLElementImpl htmlElementImpl = getHtmlElement();
            htmlElementImpl.getParentNode().insertBefore(((WebKitElement) element).getHtmlElement(), getHtmlElement());
        }
        return this;
    }

    @Override
    public Element prepend(String html) {
        List<Element> list = document.parseHTML(html);
        for (Element element : list) {
            WebKitElement elementImpl = (WebKitElement) element;
            getHtmlElement().insertBefore(elementImpl.getNode(), getHtmlElement().getFirstChild());
        }
        return this;
    }

    @Override
    public Element prepend(Element element) {
        if (element instanceof WebKitElement) {
            WebKitElement elementImpl = (WebKitElement) element;
            HTMLElementImpl htmlElementImpl = elementImpl.getHtmlElement();
            getHtmlElement().insertBefore(htmlElementImpl, getHtmlElement().getFirstChild());
        }
        return this;
    }

    @Override
    public boolean isHtmlElement() {
        return element instanceof HTMLElement;
    }

    @Override
    public String getInnerHTML() {
        return getHtmlElement().getInnerHTML();
    }

    @Override
    public Element setInnerHTML(String html) {
        getHtmlElement().setInnerHTML(html);
        return this;
    }

    @Override
    public Element focus() {
        getHtmlElement().focus();
        return this;
    }

    @Override
    public Element query(String selector) {
        return ((WebKitPageContext) context).getSelectorEngine(getHtmlElement().getOwnerDocument()).query(this, selector);
    }

    @Override
    public List<Element> queryAll(String selector) {
        return ((WebKitPageContext) context).getSelectorEngine(getHtmlElement().getOwnerDocument()).queryAll(this, selector);
    }

    @Override
    public Element on(String event, String selector, EventHandler handler) {
        bind(event, new DelegatingEventHandler(event, selector, handler));
        return this;
    }

    @Override
    public Element off() {
        return off(null, null);
    }

    @Override
    public Element off(String event) {
        return off(event, null);
    }

    @Override
    public Element off(String event, String selector) {
        Set<EventHandler> handlers = new HashSet<>(context.getEventRegistrar().getHandlers());
        for (EventHandler next : handlers) {
            if (next instanceof DelegatingEventHandler) {
                DelegatingEventHandler dh = (DelegatingEventHandler) next;
                if (dh.getEvent().equals(event) && dh.getSelector().equals(selector)) {
                    unbind(dh);
                } else if (dh.getEvent().equals(event) && selector == null) {
                    unbind(dh);
                } else if (event == null && selector == null) {
                    unbind(dh);
                }
            }
        }
        handlers.clear();
        return this;
    }

    @Override
    public Point getOffset() {
        DocumentImpl document = (DocumentImpl) engine.getEngine().getDocument();
        com.sun.webkit.dom.ElementImpl elementImpl = (com.sun.webkit.dom.ElementImpl) document.getBody();
        int scrollTop = elementImpl.getScrollTop();
        int scrollLeft = elementImpl.getScrollLeft();
        HTMLElementImpl htmlElementImpl = getHtmlElement();
        JSObject clientRect = (JSObject) htmlElementImpl.call("getBoundingClientRect");
        int top = (int) (Float.parseFloat(String.valueOf(clientRect.getMember("top"))) + scrollTop);
        int left = (int) (Float.parseFloat(String.valueOf(clientRect.getMember("left"))) + scrollLeft);
        return new Point(top, left);
    }

    @Override
    public Element scrollIntoView(boolean alignWithTop) {
        getHtmlElement().scrollIntoView(alignWithTop);
        return this;
    }

    @Override
    public Element setCss(Map<String, String> properties) {
        if (properties == null) {
            return this;
        }
        for (Map.Entry<String, String> next : properties.entrySet()) {
            setCss(next.getKey(), next.getValue());
        }
        return this;
    }

    @Override
    public Element setCss(String propertyName, String value) {
        return setCss(propertyName, value, "");
    }

    @Override
    public Element removeCss(String propertyName) {
        HTMLElementImpl elementImpl = (HTMLElementImpl) getHtmlElement();
        elementImpl.getStyle().removeProperty(propertyName);
        return this;
    }

    @Override
    public Element setCss(String propertyName, String value, String important) {
        HTMLElementImpl elementImpl = (HTMLElementImpl) getHtmlElement();
        elementImpl.getStyle().setProperty(propertyName, value, important);
        return this;
    }

    @Override
    public String getCss(String propertyName) {
        String value = getHtmlElement().getStyle().getPropertyValue(propertyName);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value;
    }

    @Override
    public Element detach() {
        getHtmlElement().getParentNode().removeChild(getHtmlElement());
        return this;
    }

    @Override
    public boolean isAttached() {
        DocumentImpl documentImpl = (DocumentImpl) engine.getEngine().getDocument();
        boolean attached = documentImpl.contains(element);
        return attached;
    }

    @Override
    public Element getPrev() {
        org.w3c.dom.Element prev = getHtmlElement().getPreviousElementSibling();
        if (prev != null) {
            return ((WebKitPageContext) context).createElement(prev, document, engine);
        } else {
            return null;
        }
    }

    @Override
    public Element getNext() {
        org.w3c.dom.Element next = getHtmlElement().getNextElementSibling();
        if (next != null) {
            return ((WebKitPageContext) context).createElement(next, document, engine);
        } else {
            return null;
        }
    }

    @Override
    public Element setTitle(String title) {
        getHtmlElement().setTitle(title);
        return this;
    }

    @Override
    public String getTitle() {
        String title = getHtmlElement().getTitle();
        if (title == null || title.isEmpty()) {
            return null;
        }
        return title;
    }

    @Override
    public Element setTabIndex(int index) {
        getHtmlElement().setTabIndex(index);
        return this;
    }

    @Override
    public int getTabIndex() {
        return getHtmlElement().getTabIndex();
    }

    @Override
    public boolean hasChildNodes() {
        return getNode().hasChildNodes();
    }

    public HTMLElementImpl getHtmlElement() {
        if (element instanceof HTMLElementImpl) {
            return (HTMLElementImpl) element;
        } else {
            return null;
        }
    }

    public NodeImpl getNode() {
        return (NodeImpl) element;
    }

    public PageContext getConfiguration() {
        return context;
    }

    @Override
    public boolean isEqualNode(io.webfolder.ui4j.api.dom.Node node) {
        if (node instanceof WebKitElement) {
            WebKitElement fxElementImpl = (WebKitElement) node;
            return getNode().isEqualNode(fxElementImpl.getNode());
        }
        return false;
    }

    @Override
    public boolean isSameNode(io.webfolder.ui4j.api.dom.Node node) {
        if (node instanceof WebKitElement) {
            WebKitElement fxElementImpl = (WebKitElement) node;
            return getNode().isSameNode(fxElementImpl.getNode());
        }
        return false;
    }

    @Override
    public float getOuterHeight() {
        int height = (int) getHtmlElement().getOffsetHeight();
        float marginTop = 
                Float.parseFloat(getHtmlElement().eval("parseFloat(window.getComputedStyle(this, null).marginTop, 10)").toString());
        float marginBottom = 
                Float.parseFloat(getHtmlElement().eval("parseFloat(window.getComputedStyle(this, null).marginBottom, 10)").toString());
        return height + marginTop + marginBottom;
    }

    @Override
    public float getClientHeight() {
        return (float) getHtmlElement().getClientHeight();
    }

    @Override
    public float getClientWidth() {
        return (float) getHtmlElement().getClientWidth();
    }

    @Override
    public float getOuterWidth() {
        int width = (int) getHtmlElement().getOffsetWidth();
        float marginLeft = 
                Float.parseFloat(getHtmlElement().eval("parseFloat(window.getComputedStyle(this, null).marginLeft, 10)").toString());
        float marginRight = 
                Float.parseFloat(getHtmlElement().eval("parseFloat(window.getComputedStyle(this, null).marginRight, 10)").toString());
        return width + marginLeft + marginRight;
    }

    @Override
    public Element appendTo(Element parent) {
        if (parent.isHtmlElement()) {
            parent.append(this);
        }
        return this;
    }

    @Override
    public Element hide() {
        setCss("display", "none");
        return this;
    }

    @Override
    public Element show() {
        setCss("display", "");
        return this;
    }

    @Override
    public boolean is(String selector) {
        if (((WebKitPageContext) context).getConfiguration().getSelectorEngine().equals(SelectorType.SIZZLE)) {
            String escapedSelector = selector.replace('\'', '"');
            Object matches = getHtmlElement().eval("Sizzle.matchesSelector(this, '" + escapedSelector + "')");
            return Boolean.parseBoolean(String.valueOf(matches));
        } else {
            return getHtmlElement().webkitMatchesSelector(selector);
        }
    }

    @Override
    public boolean contains(Element element) {
        if (element instanceof WebKitElement) {
            WebKitElement elementImpl = (WebKitElement) element;
            if (!elementImpl.getHtmlElement().isSameNode(getHtmlElement()) &&
                                getHtmlElement().contains(elementImpl.getHtmlElement())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Element cloneElement() {
        HTMLElementImpl htmlElementImpl = getHtmlElement();
        Node cloneNode = htmlElementImpl.cloneNode(true);
        Element cloneElement = ((WebKitPageContext) context).createElement(cloneNode, document, engine);
        return cloneElement;
    }

    @Override
    public String getOuterHTML() {
        HTMLElementImpl htmlElementImpl = getHtmlElement();
        return htmlElementImpl.getOuterHTML();
    }

    @Override
    public Element getOffsetParent() {
        HTMLElementImpl htmlElementImpl = getHtmlElement();
        org.w3c.dom.Element offsetParent = htmlElementImpl.getOffsetParent();
        return ((WebKitPageContext) context).createElement(offsetParent, document, engine);
    }

    @Override
    public Point getPosition() {
        HTMLElementImpl htmlElementImpl = getHtmlElement();
        if (htmlElementImpl != null) {
            Point point = new Point(Double.valueOf(htmlElementImpl.getOffsetLeft()).intValue(), Double.valueOf(htmlElementImpl.getOffsetTop()).intValue());
            return point;
        } else {
            return null;
        }
    }

    @Override
    public Element replaceWith(String html) {
        HTMLElementImpl htmlElementImpl = getHtmlElement();
        htmlElementImpl.setOuterHTML(html);
        return this;
    }

    @Override
    public Element replaceWith(Element element) {
        if (element instanceof WebKitElement) {
            getNode()
                .getParentNode()
                .replaceChild(((WebKitElement) element).getNode(), getNode());
        }
        return this;
    }

    @Override
    public List<Element> getSiblings(String selector) {
        Element parent = getParent();
        if (parent == null) {
            Collections.emptyList();
        }
        List<Element> children = parent.getChildren();
        List<Element> siblings = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            Element next = children.get(i);
            if (next.is(selector) && !next.isSameNode(this)) {
                siblings.add(next);
            }
        }
        if (siblings.isEmpty()) {
            return Collections.emptyList();
        }
        return siblings;
    }

    @Override
    public List<Element> getSiblings() {
        Element parent = getParent();
        if (parent == null) {
            Collections.emptyList();
        }
        List<Element> children = parent.getChildren();
        List<Element> siblings = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            Element next = children.get(i);
            if (next.isSameNode(this)) {
                continue;
            }
            siblings.add(next);
        }
        if (siblings.isEmpty()) {
            return Collections.emptyList();
        }
        return siblings;
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public Element closest(String selector) {
        HTMLElementImpl el = getHtmlElement();
        HTMLElementImpl parent = null;
        while (el != null) {
            parent = (HTMLElementImpl) el.getParentElement();
            if (parent != null) {
                Element pElement = ((WebKitPageContext) context).createElement(parent, document, engine);
                return ((WebKitPageContext) context).getSelectorEngine(getHtmlElement().getOwnerDocument()).query(pElement, selector);
            }
            el = parent;
        }
        return null;
    }

    @Override
    public Element getNextSibling() {
        HTMLElementImpl el = getHtmlElement();
        Node sibling = el.getNextElementSibling();
        if (sibling == null) {
            return null;
        } else {
            Element element = ((WebKitPageContext) context).createElement(sibling, document, engine);
            return element;
        }
    }

    public Document getContentDocument() {
        if (element instanceof HTMLFrameElement) {
            DocumentImpl documentImpl = (DocumentImpl) ((HTMLFrameElementImpl) element).getContentDocument();
            WebKitPageContext webkitPageContext = (WebKitPageContext) context;
            Document document = webkitPageContext.getContentDocument(documentImpl, engine);
            return document;
        } else if (element instanceof HTMLIFrameElement) {
            DocumentImpl documentImpl = (DocumentImpl) ((HTMLIFrameElementImpl) element).getContentDocument();
            WebKitPageContext webkitPageContext = (WebKitPageContext) context;
            Document document = webkitPageContext.getContentDocument(documentImpl, engine);
            return document;
        }
        return null;
    }

    @Override
    public String toString() {
        return "WebKitElement [element=" + this.getInnerHTML() + "]";
    }

	@Override
	public Object eval(String expression) {
		Object result = getHtmlElement().eval(expression);
		if (result instanceof JSObject) {
			return new WebKitMapper(engine).toJava((JSObject) result);
		} else {
			return result;
		}
	}

    public JavaScriptEngine getEngine() {
        return engine;
    }
}
