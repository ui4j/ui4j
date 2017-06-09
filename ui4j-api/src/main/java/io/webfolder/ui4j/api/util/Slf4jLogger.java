package io.webfolder.ui4j.api.util;

/**
 * An implementation of {@link io.webfolder.ui4j.api.util.Logger} instance.
 * 
 * <p>This is an proxy class that delegates all method calls to {@link org.slf4j.Logger}.</p>
 */
public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger log;

    public Slf4jLogger(Object log) {
        this.log = (org.slf4j.Logger) log;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slf4j.Logger#info(String)
     */
    @Override
    public void info(String message) {
        log.info(message);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slf4j.Logger#error(String)
     */
    @Override
    public void error(String message) {
        log.error(message);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slf4j.Logger#debug(String)
     */
    @Override
    public void debug(String message) {
        log.debug(message);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slf4j.Logger#error(String, Throwable)
     */
    @Override
    public void error(Throwable t) {
        log.error(t.getMessage(), t);
    }
}
