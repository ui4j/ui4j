package io.webfolder.ui4j.webkit.spi;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.w3c.dom.events.EventListener;

import io.webfolder.ui4j.api.dom.EventTarget;
import io.webfolder.ui4j.api.event.EventHandler;
import io.webfolder.ui4j.spi.EventRegistrar;
import io.webfolder.ui4j.spi.PageContext;
import io.webfolder.ui4j.webkit.browser.WebKitEventListener;
import io.webfolder.ui4j.webkit.dom.WebKitDocument;
import io.webfolder.ui4j.webkit.dom.WebKitElement;

public class W3CEventRegistrar implements EventRegistrar {

    private PageContext context;

    private Map<EventHandler, EventListener> listeners = new WeakHashMap<>();

    public W3CEventRegistrar(PageContext context) {
        this.context = context;
    }

    @Override
    public void register(EventTarget node, String event, EventHandler handler) {
        if (node instanceof WebKitDocument) {
            node = ((WebKitDocument) node).getBody().getParent();
        }
        if (node instanceof WebKitElement) {
            WebKitElement elementImpl = (WebKitElement) node;
            WebKitEventListener listener = new WebKitEventListener(elementImpl, context, event, handler);
            listeners.put(handler, listener);
            elementImpl.setAttribute("ui4j-registered-event", "true");
            elementImpl.getNode().addEventListener(event, listener, false);
        }
    }

    @Override
    public void unregister(EventTarget node, String event, EventHandler handler) {
        if (node instanceof WebKitDocument) {
            node = ((WebKitDocument) node).getBody().getParent();
        }
        if (node instanceof WebKitElement) {
            WebKitElement elementImpl = (WebKitElement) node;
            EventListener listener = listeners.get(handler);
            listeners.remove(handler);
            elementImpl.removeAttribute("ui4j-registered-event");
            elementImpl.getNode().removeEventListener(event, listener, false);
        }
    }

    @Override
    public Set<EventHandler> getHandlers() {
        return Collections.unmodifiableSet(listeners.keySet());
    }
}
