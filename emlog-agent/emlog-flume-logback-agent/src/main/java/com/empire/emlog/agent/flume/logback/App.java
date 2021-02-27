package com.empire.emlog.agent.flume.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author auron.xu
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("bbb{}", "sssssssss");
        getMain();
    }

    private static void getMain() {
        logger.info("bbbxxxxxxxxxxxx{}", "sssssssss");
        logger.error("ssss", new RuntimeException("xx"));
    }
}
