package io.webfolder.ui4j.api.dom;

import java.util.List;

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
                String attribute = next.getAttribute("type");
                if (attribute != null) {
                    String type = attribute.trim();
                    if (type.equalsIgnoreCase("radio")) {
                        next.getRadioButton().setChecked(false);
                    } else if (type.equalsIgnoreCase("checkbox")) {
                        next.getCheckBox().setChecked(false);
                    } else if (type.equalsIgnoreCase("text")) {
                        next.setValue("");
                    }
                } else {
                    next.setValue("");
                }
            } else if (tag.equals("select")) {
                next.getSelect().clearSelection();
            } else if (tag.equals("textarea")) {
                next.setText("");
            }
        }
    }

    public Element getElement() {
        return element;
    }

    public void submit() {
    	element.eval("this.submit()");
    }
}
