package com.ui4j.webkit.dom;

import netscape.javascript.JSObject;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;

import com.ui4j.spi.JavaScriptEngine;

class WebKitHtmlParser {

    private JavaScriptEngine engine;

    static class DefaultNodeList implements NodeList {

        private JSObject obj;

        private int length;

        public DefaultNodeList(JSObject obj) {
            this.obj = obj;
            this.length = Integer.parseInt(obj.getMember("length").toString());
        }

        @Override
        public Node item(int index) {
            return (Node) obj.getMember(String.valueOf(index));
        }

        @Override
        public int getLength() {
            return length;
        }
    }

    WebKitHtmlParser(JavaScriptEngine engine) {
        this.engine = engine;
    }

    NodeList parse(String html, HTMLDocument document) {
        JSObject wrapperDiv = (JSObject) engine.executeScript("document.createElement('div')");
        wrapperDiv.setMember("innerHTML", html);
        JSObject childNodes = (JSObject) wrapperDiv.getMember("childNodes");
        NodeList list = new DefaultNodeList(childNodes);
        return list;
    }
}
