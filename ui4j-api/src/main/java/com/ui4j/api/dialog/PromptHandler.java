package com.ui4j.api.dialog;

@FunctionalInterface
public interface PromptHandler {

    String handle(PromptDialogEvent event);
}
