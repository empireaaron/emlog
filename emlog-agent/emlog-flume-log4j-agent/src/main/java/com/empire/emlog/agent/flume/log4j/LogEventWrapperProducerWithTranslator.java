package com.empire.emlog.agent.flume.log4j;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * @author aaron.xu
 */
class LogEventWrapperProducerWithTranslator {
    /**
     * 一个translator可以看做一个事件初始化器，publicEvent方法会调用它，填充Event
     */
    private static final EventTranslatorOneArg<LogEventWrapper, LoggingEvent> TRANSLATOR =
        new EventTranslatorOneArg<LogEventWrapper, LoggingEvent>() {
            @Override
            public void translateTo(LogEventWrapper event, long sequence, LoggingEvent logEvent) {
                event.setLoggerName(logEvent.getLoggerName());
                event.setLevel(logEvent.getLevel().toString());
                event.setTimeMillis(logEvent.getTimeStamp());
                event.setThreadName(logEvent.getThreadName());
                event.setMessage(logEvent.getMessage().toString());
                // event.setParameters(logEvent.get);
                event.setThrown(logEvent.getThrowableInformation() == null ? null
                    : logEvent.getThrowableInformation().getThrowable());
                if (LogEventWrapperProducerWithTranslator.WRITE_LOG_LOCATION) {
                    LocationInfo locationInfo = logEvent.getLocationInformation();
                    event.setMethod(locationInfo.getMethodName());
                    event.setLineNumber(Integer.parseInt(locationInfo.getLineNumber()));
                }
            }
        };

    private final RingBuffer<LogEventWrapper> ringBuffer;

    private static Boolean WRITE_LOG_LOCATION = null;

    LogEventWrapperProducerWithTranslator(RingBuffer<LogEventWrapper> ringBuffer, Boolean writeLogLocation) {
        this.ringBuffer = ringBuffer;
        WRITE_LOG_LOCATION = writeLogLocation;
    }

    void onData(LoggingEvent logEvent) {
        ringBuffer.publishEvent(TRANSLATOR, logEvent);
    }
}