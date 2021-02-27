package com.empire.emlog.agent.flume.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author auron.xu
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("bbb{}", "sssssssss");
        logger.info("traceId:" + TraceIdGenerator.generate());
    }
}
