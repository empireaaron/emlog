//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.empire.emlog.agent.flume.log4j2.agent;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

/**
 * @author aaron.xu
 */
@Plugin(name = "flumeAgentConfig", category = "Core", printObject = true)
public final class FlumeAgentConfig {
    private static final Logger logger = StatusLogger.getLogger();
    private FlumeChannel channel;
    private FlumeSink sink;

    FlumeAgentConfig() {
        logger.warn("flume channel & sink config cannot be null, it will initialize by default");
        channel = new FlumeChannel();
        sink = new FlumeSink();
    }

    private FlumeAgentConfig(FlumeChannel channel, FlumeSink sink) {
        this.channel = channel;
        this.sink = sink;
    }

    FlumeChannel getChannel() {
        return channel;
    }

    FlumeSink getSink() {
        return sink;
    }

    @PluginFactory
    public static FlumeAgentConfig createFlumeAgentConfig(@PluginElement("channel") FlumeChannel channel,
        @PluginElement("sink") FlumeSink sink, @PluginAttribute("servers") String servers,
        @PluginAttribute("local_cache_dir") String localCacheDir) {
        if (channel == null) {
            logger.warn("flume channel config cannot be null, it will initialize by default");
            channel = new FlumeChannel();
        }
        if (sink == null) {
            logger.warn("flume sink config cannot be null, it will initialize by default");
            sink = new FlumeSink();
        }
        if (StringUtils.isNotBlank(servers)) {
            sink.setServers(servers);
        }
        if (StringUtils.isNotBlank(localCacheDir)) {
            channel.setLocalCacheDir(localCacheDir);
        }
        return new FlumeAgentConfig(channel, sink);
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

}
