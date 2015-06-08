package com.ui4j.api.dom;

import java.util.List;
import java.util.Optional;

public class Form {

    private Element element;

    public Form(Element element) {
        this.element = element;
    }

    public void clear() {
        List<Element> inputs = element.find("input, select, textarea");
        for (Element next : inputs) {
            String tag = next.getTagName();
            if (tag.equals("input")) {
                Optional<String> attribute = next.getAttribute("type");
                if (attribute.isPresent()) {
                    String type = attribute.get();
                    if (type.equalsIgnoreCase("radio")) {
                        next.getRadioButton().get().setChecked(false);
                    } else if (type.equalsIgnoreCase("checkbox")) {
                        next.getCheckBox().get().setChecked(false);
                    }
                } else {
                    next.setValue("");
                }
            } else if (tag.equals("select")) {
                next.getSelect().get().clearSelection();
            } else if (tag.equals("textarea")) {
                next.setText("");
            }
        }
    }

    public Element getElement() {
        return element;
    }
}
