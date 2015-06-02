package com.ui4j.spi;

import java.util.concurrent.TimeUnit;

import com.ui4j.api.util.Ui4jException;

public class Ui4jExecutionTimeoutException extends Ui4jException {

    private int elapsedTime;

    private TimeUnit timeUnit;

    private static final long serialVersionUID = 1L;

    public Ui4jExecutionTimeoutException(InterruptedException e,
                        int elapsedTime, TimeUnit timeUnit) {
        super(e);
        this.elapsedTime = elapsedTime;
        this.timeUnit = timeUnit;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
