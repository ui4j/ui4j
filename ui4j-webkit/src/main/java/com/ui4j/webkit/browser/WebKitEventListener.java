package com.ui4j.webkit.browser;

import netscape.javascript.JSObject;

import org.w3c.dom.Node;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MouseEvent;

import com.sun.webkit.dom.MouseEventImpl;
import com.sun.webkit.dom.NodeImpl;
import com.ui4j.api.event.EventAdapter;
import com.ui4j.api.event.EventHandler;
import com.ui4j.spi.PageContext;
import com.ui4j.webkit.dom.WebKitElement;

public class WebKitEventListener implements EventListener {

    protected WebKitElement element;

    protected EventHandler handler;

    protected String listenerId;

    protected String event;

    private PageContext context;

    public WebKitEventListener(WebKitElement element, PageContext configuration, String event, EventHandler handler) {
        this.element = element;
        this.context = configuration;
        this.handler = handler;
        this.event = event;
    }

    @Override
    public void handleEvent(final org.w3c.dom.events.Event evt) {
        com.sun.webkit.dom.EventImpl eventImpl = (com.sun.webkit.dom.EventImpl) evt;
        Node target = (Node) evt.getTarget();
        Node currentTarget = (Node) evt.getCurrentTarget();
        WebKitElement elementImpl = (WebKitElement) ((WebKitPageContext) context).createElement(target, element.getDocument(), element.getEngine());
        WebKitElement currentElementImpl = (WebKitElement) ((WebKitPageContext) context).createElement(currentTarget, element.getDocument(), element.getEngine());
        EventAdapter ui4jEvent = new EventAdapter(evt.getType(), elementImpl, currentElementImpl);
        if (eventImpl instanceof MouseEventImpl) {
            MouseEventImpl mouseEventImpl = (MouseEventImpl) eventImpl;

            NodeImpl nodeImpl = (NodeImpl) target;
            JSObject rect = (JSObject) nodeImpl.eval("this.getBoundingClientRect()");

            // http://www.jacklmoore.com/notes/mouse-position
            int left = (int) Math.round(Double.parseDouble(rect.getMember("left").toString()));
            int top = (int) Math.round(Double.parseDouble(rect.getMember("top").toString()));

            int offsetX = mouseEventImpl.getClientX() - left;
            int offsetY = mouseEventImpl.getClientY() - top;

            ui4jEvent.setOffsetX(offsetX);
            ui4jEvent.setOffsetY(offsetY);

            if (evt instanceof MouseEvent) {
                MouseEvent me = (MouseEvent) evt;
                ui4jEvent.setClientX(me.getClientX());
                ui4jEvent.setClientY(me.getClientY());
            }
        }
        handler.handle(ui4jEvent);
    }

    public EventHandler getEventHandler() {
        return handler;
    }

    public WebKitElement getElement() {
        return element;
    }
}
