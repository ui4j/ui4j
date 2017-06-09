package io.webfolder.ui4j.api.dialog;

import io.webfolder.ui4j.api.util.Logger;
import io.webfolder.ui4j.api.util.LoggerFactory;

class AcceptingConfirmDialogHandler implements ConfirmHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AcceptingConfirmDialogHandler.class);

    @Override
    public boolean handle(DialogEvent event) {
        LOG.info("Replying [true] to message: " + event.getMessage());
        return true;
    }
}
