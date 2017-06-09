package io.webfolder.ui4j.api.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.Thread.currentThread;

/**
 * A factory from which {@link Logger} instances can be obtained.
 * 
 * <p>All of this class's public methods are thread-safe.</p>
 */
public class LoggerFactory {

    private static Class<?> SLF4J_LOGGER_CLASS;

    static {
        try {
            SLF4J_LOGGER_CLASS = currentThread().
                                    getContextClassLoader().
                                    loadClass("org.slf4j.LoggerFactory");
        } catch (ClassNotFoundException e) {
            // ignore
        }
    }

    /**
     * 
     * @param klass the returned logger will be named after klass.
     * @return logger
     */
    public static synchronized Logger getLogger(Class<?> klass) {
        boolean foundSlf4j = SLF4J_LOGGER_CLASS != null;
        if (foundSlf4j) {
            Method getLoggerMethod = null;
            try {
                getLoggerMethod = SLF4J_LOGGER_CLASS.getMethod("getLogger", Class.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new Ui4jException(e);
            }
            Object slf4jLogger = null;
            try {
                slf4jLogger = getLoggerMethod.invoke(null, klass);
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new Ui4jException(e);
            }
            return new Slf4jLogger(slf4jLogger);
        } else {
            return new JulLogger(klass);
        }
    }
}
