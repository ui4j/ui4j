package io.webfolder.ui4j.api.dialog;

public class DialogEvent {

    private String message;

    public DialogEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DialogEvent [message=" + message + "]";
    }
}
