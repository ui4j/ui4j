package io.webfolder.ui4j.api.dialog;

import io.webfolder.ui4j.api.util.Logger;
import io.webfolder.ui4j.api.util.LoggerFactory;

class RejectingConfirmDialogHandler implements ConfirmHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RejectingConfirmDialogHandler.class);

    @Override
    public boolean handle(DialogEvent event) {
        LOG.info("Replying [false] to message: " + event.getMessage());
        return false;
    }
}
