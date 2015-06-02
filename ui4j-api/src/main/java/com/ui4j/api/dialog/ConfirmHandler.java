package com.ui4j.api.dialog;

@FunctionalInterface
public interface ConfirmHandler {

    boolean handle(DialogEvent event);
}
