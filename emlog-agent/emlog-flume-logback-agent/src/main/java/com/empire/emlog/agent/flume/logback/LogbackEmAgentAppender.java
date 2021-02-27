package com.empire.emlog.agent.flume.logback;

import java.util.Map;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.flume.agent.embedded.EmbeddedAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * @author aaron.xu
 */
public class LogbackEmAgentAppender extends AppenderBase<ILoggingEvent> {
    private static final Logger logger = LoggerFactory.getLogger(FlumeAgentConfig.class);
    private String appName;
    private String servers;
    private String sourceIp;
    private String localCacheDir;
    private int agentNum = 1;
    private boolean debug = false;
    private boolean writeLogLocation = true;

    private FlumeAgentConfig flumeAgentConfig;
    private EmbeddedAgent agent = null;
    private LogEventWrapperProducerWithTranslator producer;

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isWriteLogLocation() {
        return writeLogLocation;
    }

    public void setWriteLogLocation(boolean writeLogLocation) {
        this.writeLogLocation = writeLogLocation;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public FlumeAgentConfig getFlumeAgentConfig() {
        return flumeAgentConfig;
    }

    public void setFlumeAgentConfig(FlumeAgentConfig flumeAgentConfig) {
        this.flumeAgentConfig = flumeAgentConfig;
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

    public int getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(int agentNum) {
        this.agentNum = agentNum;
    }

    public EmbeddedAgent getAgent() {
        return agent;
    }

    public void setAgent(EmbeddedAgent agent) {
        this.agent = agent;
    }

    public LogEventWrapperProducerWithTranslator getProducer() {
        return producer;
    }

    private void setProducer(LogEventWrapperProducerWithTranslator producer) {
        this.producer = producer;
    }

    @Override
    protected void append(ILoggingEvent logEvent) {
        producer.onData(logEvent);
    }

    @Override
    public void start() {
        super.start();
        // flume config init
        if (flumeAgentConfig == null) {
            flumeAgentConfig = new FlumeAgentConfig();
        }
        if (StringUtils.isNotBlank(servers)) {
            flumeAgentConfig.getSink().setServers(servers);
        }
        if (StringUtils.isNotBlank(localCacheDir)) {
            flumeAgentConfig.getChannel().setLocalCacheDir(localCacheDir);
        }
        flumeAgentConfig.init();
        // flume config check
        if (!flumeAgentConfig.checkConf()) {
            logger.error("flumeAgentConfig配置必填参数缺失:channel[local_cache_dir],Sink[servers]");
        }

        logger.warn(flumeAgentConfig.toString());

        if (sourceIp == null) {
            sourceIp = IpUtil.getIp();
        }
        doInit();
        System.out.println("flumeAgentConfig");
    }

    private void doInit() {

            LogbackEmAgentConfiguration log4j2EmAgentConfiguration = new LogbackEmAgentConfiguration();
            Map<String, String> conf = log4j2EmAgentConfiguration.configure(this.getName(), this.flumeAgentConfig);
            String agentName = conf.get("agent.name");
            conf.remove("agent.name");
            conf.remove("lcd");
            this.agent = new EmbeddedAgent(agentName);
            this.agent.configure(conf);
            this.agent.start();
            logger.info(agentName + ' ' + this.agent + " start.");

            // RingBuffer 大小，必须是 2 的 N 次方；
            int ringBufferSize = 1024 * 1024;
            EventFactory<LogEventWrapper> eventFactory = new LogEventWrapperFactory();
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Disruptor-ThreadPool").build();
            final Disruptor<LogEventWrapper> disruptor = new Disruptor<>(eventFactory, ringBufferSize,
                namedThreadFactory, ProducerType.SINGLE, new YieldingWaitStrategy());
            EventHandler<LogEventWrapper> eventHandler =
                new LogEventHandler(this.agent, this.appName, this.sourceIp, this.debug);
            disruptor.setDefaultExceptionHandler(new EventExceptionHandler());
            disruptor.handleEventsWith(eventHandler);
            disruptor.start();
            logger.info("flume-logback-em-agent KafkaAppender disruptor Started.");
            RingBuffer<LogEventWrapper> ringBuffer = disruptor.getRingBuffer();
            LogEventWrapperProducerWithTranslator producer =
                new LogEventWrapperProducerWithTranslator(ringBuffer, writeLogLocation);
            this.setProducer(producer);
            // 应用关闭前关闭disrupt
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    disruptor.shutdown();
                    logger.info("flume-logback-em-agent KafkaAppender disruptor.shutdown");
                }
            }));

            logger.info("日志系统:flume-logback-em-agent KafkaAppender Started!!!");
    }
}
