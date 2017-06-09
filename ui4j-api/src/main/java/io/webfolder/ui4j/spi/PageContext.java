package io.webfolder.ui4j.spi;

import io.webfolder.ui4j.api.browser.SelectorEngine;

public interface PageContext {

    EventRegistrar getEventRegistrar();

    EventManager getEventManager();

    SelectorEngine getSelectorEngine();
}
