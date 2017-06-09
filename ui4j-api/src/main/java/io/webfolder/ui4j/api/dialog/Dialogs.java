package io.webfolder.ui4j.api.dialog;

public class Dialogs {

    public static AlertHandler LOGGING_ALERT_HANDLER = new AlertDialogHandler();

    public static PromptHandler DEFAULT_PROMPT_HANDLER = new PromptDialogHandler();

    public static ConfirmHandler ACCEPTING_CONFIRM_HANDLER = new AcceptingConfirmDialogHandler();

    public static ConfirmHandler REJECTING_CONFIRM_HANDLER = new RejectingConfirmDialogHandler();
}
