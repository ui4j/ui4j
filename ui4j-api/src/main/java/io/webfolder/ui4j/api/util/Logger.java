package io.webfolder.ui4j.api.util;

public interface Logger {

    /**
     * Log a message at the <b>info</b> level.
     *
     * @param message the message string to be logged
     */
    void info(String message);

    /**
     * Log a message at the <b>error</b> level.
     *
     * @param message the message string to be logged
     */
    void error(String message);

    /**
     * Log a message at the <b>debug</b> level.
     *
     * @param message the message string to be logged
     */
    void debug(String message);

    /**
     * Log an exception (throwable) at the <b>error</b> level with an
     * accompanying message.
     *
     * @param t the exception (throwable) to log
     */
    void error(Throwable t);
}
