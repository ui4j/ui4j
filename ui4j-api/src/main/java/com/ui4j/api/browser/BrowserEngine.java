package com.ui4j.api.browser;

/**
 * A web-browser engine capable of navigating to URLs and retrieving their content as {@link Page}
 * instances.
 *
 * <p>Instances of this class should normally be obtained from the static
 * {@link BrowserFactory#getWebKit()} or {@link BrowserFactory#getBrowser(BrowserType)} methods.</p>
 *
 * <p>When an instance is no longer required, its {@link #shutdown()} method should be called so
 * as to allow Ui4j to cleanly terminate its use and release any associated resources (typically,
 * client code should do this in the "finally" clause of a "try" block so as to ensure that the
 * instance is shutdown even if an exception occurs during its use). No further calls should be made
 * to an instance's methods once its {@link #shutdown()} method has been called (refer to the
 * {@link #shutdown()} method for further details).</p>
 *
 * <p>Implementations of this interface are not required to be thread-safe, and callers should
 * therefore assume that instances of this interface are not thread-safe.</p>
 */
public interface BrowserEngine {

    /**
     * Terminates the use of this instance.
     *
     * <p>Invocation of this method indicates to Ui4j that it can terminate/release any resources,
     * services, threads etc that were needed for this instance but are not otherwise still
     * required.</p>
     *
     * <p>Once this method has been called, no further calls should be made to any of this
     * instance's methods (including this {@link #shutdown()} method), and the behaviour of any such
     * calls to an already-shutdown instance's methods is entirely undefined and
     * implementation-dependent (but can potentially include the throwing of arbitrary exceptions
     * and/or causing the invoking thread or other threads to "hang").</p>
     */
    void shutdown();

    /**
     * Constructs and returns a {@link Page} representing the web-page content obtained by this
     * {@link BrowserEngine} from a given URL, using a default browser configuration for this
     * particular page.
     *
     * <p>This method is defined as exactly equivalent to calling this instance's
     * {@link #navigate(String, PageConfiguration)} method with the given URL string and a
     * newly-constructed {@link PageConfiguration} with none of its properties changed from their
     * default values.</p>
     *
     * <p>Note that the precise restrictions on which URL strings are accepted by this method
     * are undefined and entirely implementation-dependent. Similarly, this method's behaviour when
     * given a URL that it regards as invalid, or whose content cannot be successfully retrieved, or
     * whose content cannot be interpreted by this {@link BrowserEngine}, is undefined and entirely
     * implementation-dependent (but may include, for example, throwing any type of unchecked
     * exception).</p>
     *
     * @param url    The URL whose content is to be returned. May be null, or an empty or
     *     all-whitespace string, or the case-sensitive string "about:blank". Otherwise this should
     *     normally be a valid URL string for a URL  whose content is an HTML web-page or is
     *     otherwise suitable for interpretation by a web browser (however, the precise values
     *     permitted, any validation of this argument, and this method's behaviour if it considers
     *     the given value to be invalid or to be a URL that cannot be accessed or whose content
     *     this {@link BrowserEngine} cannot interpret are all undefined and entirely
     *     implementation-dependent).
     * @return    A non-null {@link Page} encapsulating the content obtained by this
     *     {@link BrowserEngine} from the given {@code url} using the default browser configuration
     *     as defined above, or a {@link Page} representing a blank/empty web page if the given
     *     {@code url} is null, an empty or all-whitespace string, or the case-sensitive string
     *     "about:blank". Note that if the given {@code url} is not a valid URL string or if the
     *     given {@code url}'s content cannot be retrieved for any reason or cannot be interpreted
     *     by this {@link BrowserEngine}, the precise nature and content of any returned
     *     {@link Page} is implementation-dependent (for example, some implementations may return a
     *     valid but empty {@link Page} in some such situations; other implementations may return a
     *     non-empty {@link Page} whose own methods fail with various exceptions).
     */
    Page navigate(String url);

    /**
     * Constructs and returns a {@link Page} representing the web-page content obtained by this
     * {@link BrowserEngine} from a given URL, using a given browser configuration for this
     * particular page.
     *
     * <p>URL values of null, empty or all-whitespace strings, and the case-sensitive string
     * "about:blank" are treated as special URLs that specify a valid but empty/blank HTML web
     * page.</p>
     *
     * <p>Note that the precise restrictions on which URL strings are accepted by this method
     * are undefined and entirely implementation-dependent. Similarly, this method's behaviour when
     * given a URL that it regards as invalid, or whose content cannot be successfully retrieved, or
     * whose content cannot be interpreted by this {@link BrowserEngine}, is undefined and entirely
     * implementation-dependent (but may include, for example, throwing any type of unchecked
     * exception).</p>
     *
     * @param url    The URL whose content is to be returned. May be null, or an empty or
     *     all-whitespace string, or the case-sensitive string "about:blank". Otherwise this should
     *     normally be a valid URL string for a URL whose content is an HTML web-page or is
     *     otherwise suitable for interpretation by a web browser (however, the precise values
     *     permitted, any validation of this argument, and this method's behaviour if it considers
     *     the given value to be invalid or to be a URL that cannot be accessed or whose content
     *     this {@link BrowserEngine} cannot interpret are all undefined and entirely
     *     implementation-dependent).
     * @param configuration    The specific browser configuration that should be used for requesting
     *     the specified {@code url} and for configuring the resulting {@link Page}. Must be
     *     non-null (and the behaviour of this method if passed null for this argument is
     *     implementation-dependent). Note that the effect of any changes to this
     *     {@link PageConfiguration} either concurrently during the execution of this method or
     *     subsequently during the use of any resulting {@link Page} is undefined and
     *     implementation-dependent, and callers should therefore avoid making any changes to the
     *     given {@link PageConfiguration} after passing it to this method (or should provide
     *     suitable locking or synchronisation if making any such changes).
     * @return    A non-null {@link Page} encapsulating the content obtained by this
     *     {@link BrowserEngine} from the given {@code url} using the given {@code configuration} as
     *     the browser configuration, or a {@link Page} representing a blank/empty web page if the
     *     given {@code url} is null, an empty or all-whitespace string, or the case-sensitive
     *     string "about:blank". Note that if the given {@code url} is not a valid URL string or if
     *     the given {@code url}'s content cannot be retrieved for any reason or cannot be
     *     interpreted by this {@link BrowserEngine}, the precise nature and content of any returned
     *     {@link Page} is implementation-dependent (for example, some implementations may return a
     *     valid but empty {@link Page} in some such situations; other implementations may return a
     *     non-empty {@link Page} whose own methods fail with various exceptions).
     */
    Page navigate(String url, PageConfiguration configuration);

    /**
     * Indicates the type of this browser engine.
     *
     * @return    The non-null {@link BrowserType} that identifies the type of the browser engine
     *     that is implemented by this instance.
     */
    BrowserType getBrowserType();
}
