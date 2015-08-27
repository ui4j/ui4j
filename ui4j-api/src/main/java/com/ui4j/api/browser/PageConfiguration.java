package com.ui4j.api.browser;

import com.ui4j.api.dialog.AlertHandler;
import com.ui4j.api.dialog.ConfirmHandler;
import com.ui4j.api.dialog.PromptHandler;
import com.ui4j.api.interceptor.Interceptor;

/**
 * The browser configuration to be used for requesting and processing a web page. This includes the
 * HTTP "User-Agent" string to be presented by the browser and which type of CSS selector engine
 * should be used for processing CSS "selector" statements.
 *
 * <p>This class is not guaranteed to be thread-safe.</p>
 */
public class PageConfiguration {

    private SelectorType selectorEngine = SelectorType.W3C;

    private String userAgent;

    private Interceptor interceptor;

    private AlertHandler alertHandler;

    private ConfirmHandler confirmHandler;

    private PromptHandler promptHandler;

    private boolean interceptAllRequests;

    public PageConfiguration() {
        this(null);
    }

    public PageConfiguration(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * Returns the type of CSS selector engine to be used when processing a web page.
     *
     * @return    The {@link SelectorType} that was passed to the most recent call to this
     *     instance's {@link #setSelectorEngine(SelectorType)} method (which can potentially be
     *     null), or a default value of {@link SelectorType#W3C} if that method has not yet been
     *     called on this instance.
     */
    public SelectorType getSelectorEngine() {
        return selectorEngine;
    }

    /**
     * Sets the type of CSS selector engine to be used when processing a web page.
     *
     * @param selectorEngine    The {@link SelectorType} to be used when processing a web page
     *     using this instance. This should normally be non-null (a null value is permitted by
     *     this class, but any subsequent use of this instance in the processing of a web page is
     *     then likely to result in a {@link NullPointerException} or other such failure - even
     *     where no CSS is involved).
     */
    public PageConfiguration setSelectorEngine(SelectorType selectorEngine) {
        this.selectorEngine = selectorEngine;
        return this;
    }

    /**
     * Returns the HTTP "User-Agent" string that should be presented by the browser when requesting
     * a web page.
     *
     * @return    The string passed to the most recent call to this instance's
     *     {@link #setUserAgent(String)} method (which can potentially be null, an empty string, or
     *     any other string value), or a default value of null if that method has not yet been
     *     called on this instance. A value of null indicates that the {@link BrowserEngine} being
     *     used should use its own default user-agent string (which itself may vary depending on the
     *     underlying JVM, JavaFX implementation, operating system, or other factors).
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the HTTP "User-Agent" string that should be presented by the browser when requesting
     * a web page.
     *
     * @param userAgent    The HTTP "User-Agent" string to be presented by the browser when
     *     requesting processing a web page using this instance. May be null to indicate that no
     *     specific user-agent string is required and that the {@link BrowserEngine} being used
     *     should use its own default user-agent string (which itself may vary depending on the
     *     underlying JVM, JavaFX implementation, operating system, or other factors). If non-null
     *     this should normally be a non-empty, non-whitespace string that consists entirely of
     *     characters that are permitted within HTTP header values and that conforms to the syntax
     *     for an HTTP "User-Agent" header value (as specified by
     *     <a href="http://tools.ietf.org/html/rfc7231#section-5.5.3">Section 5.5.3 of HTTP/1.1
     *     Semantics and Content</a>). However, this method will accept any string value, including
     *     empty or all-whitespace strings.
     */
    public PageConfiguration setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public PageConfiguration setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public PageConfiguration setAlertHandler(AlertHandler alertHandler) {
        this.alertHandler = alertHandler;
        return this;
    }

    public AlertHandler getAlertHandler() {
        return alertHandler;
    }    

    public ConfirmHandler getConfirmHandler() {
        return confirmHandler;
    }

    public PageConfiguration setConfirmHandler(ConfirmHandler confirmHandler) {
        this.confirmHandler = confirmHandler;
        return this;
    }

    public PromptHandler getPromptHandler() {
        return promptHandler;
    }

    public PageConfiguration setPromptHandler(PromptHandler promptHandler) {
        this.promptHandler = promptHandler;
        return this;
    }

    public boolean isInterceptAllRequests() {
        return interceptAllRequests;
    }

    public void setInterceptAllRequests(boolean interceptAllRequests) {
        this.interceptAllRequests = interceptAllRequests;
    }

    @Override
    public String toString() {
        return "PageConfiguration [selectorEngine=" + selectorEngine
                + ", userAgent=" + userAgent + ", interceptor=" + interceptor
                + ", alertHandler=" + alertHandler + ", confirmHandler="
                + confirmHandler + ", promptHandler=" + promptHandler
                + ", interceptAllRequests=" + interceptAllRequests + "]";
    }
}
