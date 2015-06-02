package com.ui4j.api.dom;

import java.util.List;

public class Form {

    private Element element;

    public Form(Element element) {
        this.element = element;
    }

    public void clear() {
        List<Element> inputs = element.find("input, select");
        for (Element next : inputs) {
            if (next.getTagName().equals("input")) {
                String type = next.getAttribute("type");
                if (type.trim().isEmpty() || type.equalsIgnoreCase("text")) {
                    next.setValue("");
                } else if (type.equalsIgnoreCase("radio")) {
                    next.getRadioButton().get().setChecked(false);
                } else if (type.equalsIgnoreCase("checkbox")) {
                    next.getCheckBox().get().setChecked(false);
                }
            } else if (next.getTagName().equalsIgnoreCase("select")) {
                next.getSelect().get().clearSelection();
            }
        }
    }

    public Element getElement() {
        return element;
    }
}
