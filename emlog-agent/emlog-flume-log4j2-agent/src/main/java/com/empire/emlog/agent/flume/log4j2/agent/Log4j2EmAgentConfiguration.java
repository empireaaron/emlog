package com.empire.emlog.agent.flume.log4j2.agent;

import java.util.Map;

/**
 * @author aaron.xu
 */
public class Log4j2EmAgentConfiguration extends AbstractConfiguration {

    @Override
    protected Map<String, String> collectSpecificChannelConf() {
        return null;
    }

    @Override
    protected Map<String, String> collectSpecificSinkConf() {
        return null;
    }
}
