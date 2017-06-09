package io.webfolder.ui4j.api.event;

import io.webfolder.ui4j.api.dom.Element;

public class EventAdapter implements DomEvent {

    private String type;

    private Element target;

    private Element currentTarget;

    private int offsetX;

    private int offsetY;

    private int clientX;

    private int clientY;

    public EventAdapter(String type, Element target, Element currentTarget) {
        this.type = type;
        this.target = target;
        this.currentTarget = currentTarget;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Element getTarget() {
        return target;
    }

    @Override
    public Element getCurrentTarget() {
        return currentTarget;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public int getOffsetX() {
        return offsetX;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    public int getClientX() {
        return clientX;
    }

    public void setClientX(int clientX) {
        this.clientX = clientX;
    }

    public int getClientY() {
        return clientY;
    }

    public void setClientY(int clientY) {
        this.clientY = clientY;
    }

    @Override
    public String toString() {
        return "EventAdapter [type=" + type + ", target=" + target
                + ", currentTarget=" + currentTarget + ", offsetX=" + offsetX
                + ", offsetY=" + offsetY + ", clientX=" + clientX
                + ", clientY=" + clientY + "]";
    }
}
