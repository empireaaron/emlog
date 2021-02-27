package com.empire.emlog.agent.flume.logback;

import com.lmax.disruptor.EventFactory;

/**
 * @author aaron.xu
 */
public class LogEventWrapperFactory implements EventFactory<LogEventWrapper> {
    @Override
    public LogEventWrapper newInstance() {
        return new LogEventWrapper();
    }
}
