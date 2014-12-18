package com.ui4j.api.dom;

public class CheckBox {

    private Element element;

    public CheckBox(Element element) {
        this.element = element;
    }

    public boolean isChecked() {
        String checked = String.valueOf(element.getProperty("checked"));
        return Boolean.parseBoolean(checked);
    }

    public CheckBox setChecked(boolean state) {
        element.setProperty("checked", state);
        return this;
    }

    public Element getElement() {
        return element;
    }
}
