package io.webfolder.ui4j.api.dialog;

import io.webfolder.ui4j.api.util.Logger;
import io.webfolder.ui4j.api.util.LoggerFactory;

class PromptDialogHandler implements PromptHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PromptDialogHandler.class);

    @Override
    public String handle(PromptDialogEvent event) {
        LOG.info("Javascript prompt handler message: " + event.getMessage() + ", default value: " + event.getDefaultValue());
        return event.getDefaultValue();
    }
}
