package io.webfolder.ui4j.api.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An implementation of {@link io.webfolder.ui4j.api.util.Logger} instance.
 * 
 * <p>This is an proxy class that delegates all method calls to {@link Logger}.</p>
 */
public class JulLogger implements io.webfolder.ui4j.api.util.Logger {

    private Logger log;

    public JulLogger(Class<?> klass) {
        log = java.util.logging.Logger.getLogger(klass.getName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see Level#INFO
     * @see Logger#log(Level, String)
     */
    @Override
    public void info(String message) {
        log.log(Level.INFO, message);
    }

    /**
     * {@inheritDoc}
     * 
     * @see Level#SEVERE
     * @see Logger#log(Level, String)
     */
    @Override
    public void error(String message) {
        log.log(Level.SEVERE, message);
    }

    /**
     * {@inheritDoc}
     * 
     * @see Level#FINE
     * @see Logger#log(Level, String)
     */
    @Override
    public void debug(String message) {
        log.log(Level.FINE, message);
    }

    /**
     * {@inheritDoc}
     * 
     * @see Level#SEVERE
     * @see Logger#log(Level, String, Throwable)
     */
    @Override
    public void error(Throwable t) {
        log.log(Level.SEVERE, t.getMessage(), t);
    }
}
