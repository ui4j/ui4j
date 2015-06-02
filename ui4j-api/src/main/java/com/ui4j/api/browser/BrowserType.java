package com.ui4j.api.browser;

import java.util.ServiceLoader;

import com.ui4j.spi.BrowserProvider;

/**
 * Enumeration of the types of browser engines that can potentially be used within Ui4j.
 *
 * <p>Note that this enumeration simply provides the names that are used to identify different types
 * of browser engine. Actual support for using any of these browser engine types depends on having
 * one or more corresponding {@link BrowserProvider} implementations available on the classpath and
 * suitably configured via a {@code META-INF/services/com.ui4j.spi.BrowserProvider} file (or
 * otherwise made available for discovery and loading via the {@link ServiceLoader} class). At
 * present, Ui4j provides a single built-in {@link BrowserProvider} implementation for the
 * {@link #WebKit} browser engine, via Ui4j's {@code ui4j-webkit} subproject/package.</p>
 */
public enum BrowserType {

    /**
     * The "WebKit" browser engine, as built into JavaFX as its embedded web browser (and based on
     * the open-source <a href="https://www.webkit.org/">webkit</a> project).
     */
    WebKit,

    JxBrowser
}
