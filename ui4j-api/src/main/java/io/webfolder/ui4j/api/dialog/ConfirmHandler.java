package io.webfolder.ui4j.api.dialog;

@FunctionalInterface
public interface ConfirmHandler {

    boolean handle(DialogEvent event);
}
