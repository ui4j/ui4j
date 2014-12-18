package com.ui4j.api.dom;

import static java.lang.Boolean.parseBoolean;

public class Option {

    protected Element element;

    public Option(Element element) {
        this.element = element;
    }

    public String getText() {
        return element.getText();
    }

    public String getValue() {
        return String.valueOf(element.getProperty("value"));
    }

    public Element getElement() {
        return element;
    }

    public Input getInput() {
        return getElement().getInput();
    }

    public boolean isSelected() {
        return parseBoolean(String.valueOf(element.getProperty("selected")));
    }

    public Option setSelected(boolean selected) {
        element.setProperty("selected", selected);
        return this;
    }

    public Option setText(String text) {
        getElement().setText(text);
        return this;
    }

    public Option setValue(String value) {
        getElement().setValue(value);
        return this;
    }
}
