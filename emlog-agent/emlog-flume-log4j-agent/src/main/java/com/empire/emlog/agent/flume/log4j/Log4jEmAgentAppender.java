package com.empire.emlog.agent.flume.log4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ThreadFactory;

import org.apache.flume.agent.embedded.EmbeddedAgent;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * @author aaron.xu
 */
public class Log4jEmAgentAppender extends AppenderSkeleton {

    private String appName;
    private String servers;
    private String sourceIp;
    private String localCacheDir;
    private boolean debug = false;
    private int agentNum = 1;
    private boolean writeLogLocation = true;
    private String agentFile;
    private String agentName;
    private EmbeddedAgent agent = null;
    private LogEventWrapperProducerWithTranslator producer;

    public Log4jEmAgentAppender() {}

    @Override
    protected void append(LoggingEvent event) {
        producer.onData(event);
    }

    @Override
    public void close() {}

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    public void activateOptions() {
        super.activateOptions();
        // Loading default agent configuration
        Properties defaultProps = loadDefaultAgentConf();
        // Loading agent configuration
        Properties props = loadAgentConf(agentFile);
        // properties copy
        if (localCacheDir != null && !"".equals(localCacheDir)) {
            props.setProperty("channel.local_cache_dir", localCacheDir);
        }
        if (servers != null && !"".equals(servers)) {
            props.setProperty("sink.servers", servers);
        }
        if (agentName != null && !"".equals(agentName)) {
            props.setProperty("agent.name", agentName);
        }

        if (sourceIp == null) {
            sourceIp = IpUtil.getIp();
        }
        // check agent configuration
        if (!checkConf(props)) {
            // TODO
        }
        //
        Log4jEmAgentConfiguration log4jEmAgentConfiguration = new Log4jEmAgentConfiguration();
        Map<String, String> conf = log4jEmAgentConfiguration.configure(props, defaultProps);
        start(conf);
    }

    private void start(Map<String, String> conf) {
        String agentName = conf.get("agent.name");
        conf.remove("agent.name");
        conf.remove("lcd");
        this.agent = new EmbeddedAgent(agentName);
        this.agent.configure(conf);
        this.agent.start();
        LogLog.warn(agentName + ' ' + this.agent + " start.");

        // RingBuffer 大小，必须是 2 的 N 次方；
        int ringBufferSize = 1024 * 1024;
        EventFactory<LogEventWrapper> eventFactory = new LogEventWrapperFactory();
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Disruptor-ThreadPool").build();
        final Disruptor<LogEventWrapper> disruptor = new Disruptor<>(eventFactory, ringBufferSize, namedThreadFactory,
            ProducerType.SINGLE, new YieldingWaitStrategy());
        EventHandler<LogEventWrapper> eventHandler =
            new LogEventHandler(this.agent, this.appName, this.sourceIp, this.debug);
        disruptor.setDefaultExceptionHandler(new EventExceptionHandler());
        disruptor.handleEventsWith(eventHandler);
        disruptor.start();
        LogLog.warn("flume-log4j2-em-agent KafkaAppender disruptor Started.");
        RingBuffer<LogEventWrapper> ringBuffer = disruptor.getRingBuffer();
        LogEventWrapperProducerWithTranslator producer =
            new LogEventWrapperProducerWithTranslator(ringBuffer, writeLogLocation);
        this.setProducer(producer);
        // 应用关闭前关闭disrupt
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                disruptor.shutdown();
                LogLog.warn("flume-log4j2-em-agent KafkaAppender disruptor.shutdown");
            }
        }));
        LogLog.warn("日志系统:flume-log4j2-em-agent KafkaAppender Started!!!");

    }

    private boolean checkConf(Properties props) {
        return true;
    }

    private Properties loadDefaultAgentConf() {
        return loadAgentConf("META-INF/default_agent.properties");
    }

    private Properties loadAgentConf(String agentFile) {
        Properties props = new Properties();
        if (agentFile == null || "".equals(agentFile)) {
            return props;
        }
        URL configURL = Loader.getResource(agentFile);
        LogLog.debug("Reading configuration from URL " + configURL);
        InputStream inputStream = null;
        URLConnection uConn;
        try {
            uConn = configURL.openConnection();
            uConn.setUseCaches(false);
            inputStream = uConn.getInputStream();
            props.load(inputStream);
        } catch (Exception e) {
            if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not read configuration file from URL [" + configURL + "].", e);
            LogLog.error("Ignoring configuration file [" + configURL + "].");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (InterruptedIOException ignore) {
                    Thread.currentThread().interrupt();
                } catch (IOException ignore) {
                } catch (RuntimeException ignore) {
                }
            }
        }
        return props;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getLocalCacheDir() {
        return localCacheDir;
    }

    public void setLocalCacheDir(String localCacheDir) {
        this.localCacheDir = localCacheDir;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(int agentNum) {
        this.agentNum = agentNum;
    }

    public boolean isWriteLogLocation() {
        return writeLogLocation;
    }

    public void setWriteLogLocation(boolean writeLogLocation) {
        this.writeLogLocation = writeLogLocation;
    }

    public String getAgentFile() {
        return agentFile;
    }

    public void setAgentFile(String agentFile) {
        this.agentFile = agentFile;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    private void setProducer(LogEventWrapperProducerWithTranslator producer) {
        this.producer = producer;
    }
}