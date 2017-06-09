package io.webfolder.ui4j.api.dialog;

public class PromptDialogEvent extends DialogEvent {

    private String defaultValue;

    public PromptDialogEvent(String message) {
        super(message);
    }

    public PromptDialogEvent(String message, String defaultValue) {
        this(message);
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "PromptDialogEvent [defaultValue=" + defaultValue
                + ", getMessage()=" + getMessage() + "]";
    }
}
