package com.empire.emlog.agent.flume.log4j;

import java.util.Map;

/**
 * @author auron.xu
 */
public class Log4jEmAgentConfiguration extends AbstractConfiguration {
    @Override
    protected Map<String, String> collectSpecificChannelConf() {
        return null;
    }

    @Override
    protected Map<String, String> collectSpecificSinkConf() {
        return null;
    }
}
