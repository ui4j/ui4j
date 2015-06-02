package com.ui4j.api.browser;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ui4j.api.util.Logger;
import com.ui4j.api.util.LoggerFactory;
import com.ui4j.spi.BrowserProvider;
import com.ui4j.spi.ShutdownListener;

/**
 * A factory from which {@link BrowserEngine} instances can be obtained.
 *
 * <p>Static methods are provided through which a currently-usable (i.e. not-yet-shutdown)
 * {@link BrowserEngine} of a specified {@link BrowserType} can be obtained.</p>
 *
 * <p>The static {@link #getWebKit()} method provides the simplest way to obtain an instance of
 * Ui4j's built-in {@link BrowserEngine} of type {@link BrowserType#WebKit}. This uses Ui4j's
 * {@code ui4j-webkit} subproject/packages to provide a {@link BrowserEngine} based on JavaFX's
 * built-in "webkit" browser engine.</p>
 *
 * <p>An additional static {@link #getBrowser(BrowserType)} method is provided through which
 * {@link BrowserEngine}s of other {@link BrowserType}s can be obtained, subject to suitable
 * {@link BrowserProvider} implementations for them being available on the classpath and configured
 * via a {@code META-INF/services/com.ui4j.spi.BrowserProvider} file (or otherwise made available
 * for loading via the {@link ServiceLoader} class). However, note that at present the
 * {@link BrowserType} enumeration only defines a single {@link BrowserType} of
 * {@link BrowserType#WebKit}.</p>
 *
 * <p>All of this class's public methods are thread-safe.</p>
 */
public final class BrowserFactory {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserFactory.class);

    private static final ConcurrentMap<BrowserType, BrowserEngine> BROWSERS = new ConcurrentHashMap<>();

    private static final ShutdownListener SHUTDOWN_LISTENER = new ShutdownAdapter();

    private static class ShutdownAdapter implements ShutdownListener {

        @Override
        public void onShutdown(BrowserEngine engine) {
            for (Map.Entry<BrowserType, BrowserEngine> next : BROWSERS.entrySet()) {
                if (next.getValue().equals(engine)) {
                    BROWSERS.remove(next.getKey());
                }
            }
        }
    }

    private BrowserFactory() {
    }

    /**
     * Returns a {@link BrowserEngine} of a specified {@link BrowserType}.
     *
     * <p>The {@link BrowserType} values that are supported are those for which at least one
     * {@link BrowserProvider} implementation for that {@link BrowserType} can be found at runtime
     * by the {@link ServiceLoader} class.</p>
     *
     * <p>By default, Ui4j includes a built-in {@link BrowserProvider} that supports
     * {@link BrowserEngine}s of type {@link BrowserType#WebKit}, and has a built-in
     * {@code META-INF/services/com.ui4j.spi.BrowserProvider} configuration file through which the
     * {@link ServiceLoader} class can find that {@link BrowserProvider} implementation.</p>
     *
     * <p>Refer to the {@link ServiceLoader} documentation for details of how
     * {@link BrowserProvider} implementations are found and how additional {@link BrowserProvider}
     * implementations can be made available to support other {@link BrowserType}s.</p>
     *
     * <p>Note that:</p>
     * <ul>
     *     <li>At any point in time only a single not-yet-shutdown {@link BrowserEngine} instance is
     *         maintained for each {@link BrowserType}. Repeated calls to this method for the same
     *         {@link BrowserType} will return the same {@link BrowserEngine} instance unless and
     *         until that instance's {@link BrowserEngine#shutdown()} method is called, after which
     *         the next such call to this method for that {@link BrowserType} creates and returns a
     *         new {@link BrowserEngine} instance.</li>
     *     <li>If no {@link BrowserProvider} implementation is present for the specified
     *         {@link BrowserType}, this method returns null.</li>
     *     <li>If multiple {@link BrowserProvider} implementations are present for the specified
     *         {@link BrowserType}, this method does not specify which of these will be used.</li>
     *     <li>Although this mechanism potentially supports multiple {@link BrowserType}s, at
     *         present the {@link BrowserType} enumeration only defines a single {@link BrowserType}
     *         of {@link BrowserType#WebKit}.</li>
     * </ul>
     *
     * @param type    The type of {@link BrowserEngine} to be returned. Must be non-null.
     * @return    A {@link BrowserEngine} of the given {@code type} (which is guaranteed to have not
     *     yet been shutdown), or null if no {@link BrowserProvider} is present for the given
     *     {@code type}.
     * @throws NullPointerException if the given {@code type} is null.
     * @throws ServiceConfigurationError if the {@link ServiceLoader} class throws such an
     *     exception when attempting to locate, load and instantiate the available
     *     {@link BrowserProvider} implementations (for example, if an invalid
     *     provider-configuration file is present for {@link BrowserProvider} service providers, or
     *     if any of the {@link BrowserProvider} implementations found by {@link ServiceLoader}
     *     throw exceptions when instantiated).
     */
    public static synchronized BrowserEngine getBrowser(BrowserType type) {
        if (BROWSERS.containsKey(type)) {
            return BROWSERS.get(type);
        }
        ServiceLoader<BrowserProvider> loader = ServiceLoader.load(BrowserProvider.class);
        Iterator<BrowserProvider> iter = loader.iterator();
        while (iter.hasNext()) {
            BrowserProvider provider = iter.next();
            provider.setShutdownListener(SHUTDOWN_LISTENER);
            if (type.equals(provider.getBrowserType())) {
                LOG.info("Initializing " + type);
                BrowserEngine browser = provider.create();
                BROWSERS.put(type, browser);
            }
        }
        return BROWSERS.get(type);
    }

    /**
     * Returns a {@link BrowserEngine} of type {@link BrowserType#WebKit}.
     *
     * <p>This is exactly equivalent to passing the type {@link BrowserType#WebKit} to the
     * {@link #getBrowser(BrowserType)} method.</p>
     *
     * <p>Note that as per {@link #getBrowser(BrowserType)}, at any point in time only a single
     * not-yet-shutdown {@link BrowserEngine} instance of type {@link BrowserType#WebKit} is
     * maintained. Repeated calls to this method return the same {@link BrowserEngine} instance
     * unless and until that instance's {@link BrowserEngine#shutdown()} method is called, after
     * which the next such call to this method creates and returns a new {@link BrowserEngine}
     * instance.</p>
     *
     * @return    A {@link BrowserEngine} of type {@link BrowserType#WebKit}. Note that this is
     *     normally guaranteed to be non-null, but null can potentially be returned if Ui4j's
     *     built-in configuration of a {@link BrowserProvider} for {@link BrowserType#WebKit} is not
     *     found on the classpath (or has been changed or overridden such that the relevant
     *     {@link BrowserProvider} is not found) and no alternative {@link BrowserProvider} for the
     *     {@link BrowserType#WebKit} type is present.
     * @throws ServiceConfigurationError if the {@link ServiceLoader} class throws such an
     *     exception when attempting to locate, load and instantiate the available
     *     {@link BrowserProvider} implementations (for example, if an invalid
     *     provider-configuration file is present for {@link BrowserProvider} service providers, or
     *     if any of the {@link BrowserProvider} implementations found by {@link ServiceLoader}
     *     throw exceptions when instantiated).
     *
     * @see #getBrowser(BrowserType)
     */
    public static synchronized BrowserEngine getWebKit() {
        return getBrowser(BrowserType.WebKit);
    }

    public static synchronized BrowserEngine getJxBrowser() {
        return getBrowser(BrowserType.JxBrowser);
    }
}
