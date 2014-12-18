package com.ui4j.api.dom;

public class RadioButton {

    private Element element;

    public RadioButton(Element element) {
        this.element = element;
    }

    public boolean isChecked() {
        return Boolean.parseBoolean(String.valueOf(element.getProperty("checked")));
    }

    public RadioButton setChecked(boolean state) {
        element.setProperty("checked", state);
        return this;
    }

    public Element getElement() {
        return element;
    }
}
