//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.empire.emlog.agent.flume.logback;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aaron.xu
 */
public final class FlumeAgentConfig {
    private static final Logger logger = LoggerFactory.getLogger(FlumeAgentConfig.class);
    private FlumeChannel channel;
    private FlumeSink sink;
    private String servers;
    private String localCacheDir;

    FlumeAgentConfig() {
        logger.warn("flume channel & sink config cannot be null, it will initialize by default");
        channel = new FlumeChannel();
        sink = new FlumeSink();
    }

    public FlumeAgentConfig(FlumeChannel channel, FlumeSink sink) {
        this.channel = channel;
        this.sink = sink;
    }

    FlumeChannel getChannel() {
        return channel;
    }

    FlumeSink getSink() {
        return sink;
    }

    public void setChannel(FlumeChannel channel) {
        this.channel = channel;
    }

    public void setSink(FlumeSink sink) {
        this.sink = sink;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getLocalCacheDir() {
        return localCacheDir;
    }

    public void setLocalCacheDir(String localCacheDir) {
        this.localCacheDir = localCacheDir;
    }

    @Override
    public String toString() {
        return "FlumeAgentConfig{" + "channel=" + channel + ", sink=" + sink + '}';
    }

    boolean checkConf() {
        String servers = this.getSink().getServers();
        String localCacheDir = this.getChannel().getLocalCacheDir();
        return StringUtils.isNotBlank(servers) && StringUtils.isNotBlank(localCacheDir);
    }

    void init() {
        if (StringUtils.isNotBlank(servers)) {
            sink.setServers(servers);
        }
        if (StringUtils.isNotBlank(localCacheDir)) {
            channel.setLocalCacheDir(localCacheDir);
        }
    }
}
