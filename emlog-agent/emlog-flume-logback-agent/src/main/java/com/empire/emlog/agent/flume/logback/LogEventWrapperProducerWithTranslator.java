package com.empire.emlog.agent.flume.logback;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;

/**
 * @author aaron.xu
 */
class LogEventWrapperProducerWithTranslator {
    /**
     * 一个translator可以看做一个事件初始化器，publicEvent方法会调用它，填充Event
     */
    private static final EventTranslatorOneArg<LogEventWrapper, ILoggingEvent> TRANSLATOR =
        new EventTranslatorOneArg<LogEventWrapper, ILoggingEvent>() {
            @Override
            public void translateTo(LogEventWrapper event, long sequence, ILoggingEvent logEvent) {
                event.setLoggerName(logEvent.getLoggerName());
                event.setLevel(logEvent.getLevel());
                event.setTimeMillis(logEvent.getTimeStamp());
                event.setThreadName(logEvent.getThreadName());
                event.setMessage(logEvent.getFormattedMessage());
                event.setParameters(logEvent.getArgumentArray());
                Throwable throwable = logEvent.getThrowableProxy() != null
                    ? ((ThrowableProxy)logEvent.getThrowableProxy()).getThrowable() : null;
                event.setThrown(throwable);
                if (LogEventWrapperProducerWithTranslator.WRITE_LOG_LOCATION) {
                    StackTraceElement[] st = logEvent.getCallerData();
                    event.setMethod(st[0].getMethodName());
                    event.setLineNumber(st[0].getLineNumber());
                }
            }
        };

    private final RingBuffer<LogEventWrapper> ringBuffer;

    private static Boolean WRITE_LOG_LOCATION = null;

    LogEventWrapperProducerWithTranslator(RingBuffer<LogEventWrapper> ringBuffer, Boolean writeLogLocation) {
        this.ringBuffer = ringBuffer;
        WRITE_LOG_LOCATION = writeLogLocation;
    }

    void onData(ILoggingEvent logEvent) {
        ringBuffer.publishEvent(TRANSLATOR, logEvent);
    }
}