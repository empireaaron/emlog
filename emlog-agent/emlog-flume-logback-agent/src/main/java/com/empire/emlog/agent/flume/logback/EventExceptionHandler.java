package com.empire.emlog.agent.flume.logback;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.lmax.disruptor.ExceptionHandler;

/**
 * @author aaron.xu
 */
public class EventExceptionHandler implements ExceptionHandler<LogEventWrapper> {
    private static final Logger LOGGER = Logger.getLogger(EventExceptionHandler.class.getName());
    private final Logger logger;

    EventExceptionHandler() {
        this.logger = LOGGER;
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, LogEventWrapper logEventWrapper) {
        logger.log(Level.SEVERE, "Exception processing: " + sequence + " " + logEventWrapper, ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        logger.log(Level.SEVERE, "Exception during onStart()", ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        logger.log(Level.SEVERE, "Exception during onShutdown()", ex);
    }
}