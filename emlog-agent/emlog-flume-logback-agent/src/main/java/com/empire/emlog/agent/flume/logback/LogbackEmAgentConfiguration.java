package com.empire.emlog.agent.flume.logback;

import java.util.Map;

/**
 * @author aaron.xu
 */
public class LogbackEmAgentConfiguration extends AbstractConfiguration {

    @Override
    protected Map<String, String> collectSpecificChannelConf() {
        return null;
    }

    @Override
    protected Map<String, String> collectSpecificSinkConf() {
        return null;
    }
}
