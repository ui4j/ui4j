package io.webfolder.ui4j.api.dom;

public class Input {

    protected Element element;

    public Input(Element element) {
        this.element = element;
    }

    public boolean isDisabled() {
        return Boolean.parseBoolean(String.valueOf(element.getProperty("disabled")));
    }

    public Input setDisabled(boolean disabled) {
        element.setProperty("disabled", disabled);
        return this;
    }

    public boolean isReadOnly() {
        return Boolean.parseBoolean(String.valueOf(element.getProperty("readOnly")));
    }

    public Input setReadOnly(boolean readOnly) {
        element.setProperty("readOnly", readOnly);
        return this;
    }

    public Element getElement() {
        return element;
    }

    public boolean isHidden() {
        return "hidden".equals(element.getAttribute("type"));
    }
}
