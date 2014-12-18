package com.ui4j.api.dom;

import java.util.ArrayList;
import java.util.List;

public class Select {

    private Element element;

    public Select(Element element) {
        this.element = element;
    }

    public List<Option> getOptions() {
        List<Option> options = new ArrayList<Option>();
        List<Element> list = element.getChildren();
        for (Element next : list) {
            if (next.getTagName().equals("option")) {
                options.add(next.getOption());
            }
        }
        return options;
    }

    public void clearSelection() {
        setSelectedIndex(-1);
        for (Option option : getOptions()) {
            option.setSelected(false);
        }
    }

    public Element getElement() {
        return element;
    }

    public int getSelectedIndex() {
        int selectedIndex = -1;
        for (Option option : getOptions()) {
            selectedIndex += 1;
            if (option.isSelected()) {
                return selectedIndex;
            }
        }
        return -1;
    }

    public void setSelectedIndex(int index) {
        element.setProperty("selectedIndex", index);
    }

    public Option getOption(int index) {
        List<Option> options = getOptions();
        if (options == null || options.isEmpty()) {
            return null;
        }
        return options.get(index);
    }

    public int getLength() {
        return getOptions().size();
    }

    public Option getSelection() {
        int index = getSelectedIndex();
        if (index < 0) {
            return null;
        }
        return getOptions().get(index);
    }

    public void clear() {
        element.empty();
    }

    public Select setDisabled(boolean state) {
        getElement().getInput().setDisabled(state);
        return this;
    }

    public boolean isDisabled() {
        return getElement().getInput().isDisabled();
    }

    public void change() {
        element.getDocument().trigger("change", this.getElement());
    }
}
