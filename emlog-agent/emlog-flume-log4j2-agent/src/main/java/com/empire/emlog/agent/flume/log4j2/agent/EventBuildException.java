package com.empire.emlog.agent.flume.log4j2.agent;

/**
 * @author aaron.xu
 */
public class EventBuildException extends Exception {

    public EventBuildException() {}

    public EventBuildException(String message) {
        super(message);
    }

    public EventBuildException(String message, Throwable t) {
        super(message, t);
    }

    EventBuildException(Throwable t) {
        super(t);
    }
}
