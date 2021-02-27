package com.empire.emlog.agent.flume.log4j2.agent;

import org.apache.commons.lang3.StringUtils;
import org.apache.flume.FlumeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aaron.xu
 */
public abstract class AbstractConfiguration {
    private String name;
    private FlumeAgentConfig flumeAgentConfig;

    private String getName() {
        return name;
    }

    public FlumeAgentConfig getFlumeAgentConfig() {
        return flumeAgentConfig;
    }

    Map<String, String> configure(String name, FlumeAgentConfig flumeAgentConfig) {
        this.name = name;
        this.flumeAgentConfig = flumeAgentConfig;
        return this.doConfigure();
    }

    private Map<String, String> doConfigure() {
        Map<String, String> conf = new HashMap<>(8);
        conf.putAll(this.getCollectChannelConf(flumeAgentConfig.getChannel()));
        conf.putAll(this.getCollectSinkConf(flumeAgentConfig.getSink()));
        conf.put("agent.name", this.getName());
        conf.put("lcd", flumeAgentConfig.getChannel().getLocalCacheDir());
        return conf;
    }

    private Map<String, String> getCollectChannelConf(FlumeChannel channel) {
        Map<String, String> conf = new HashMap<>(8);
        conf.put("channel.type", "file");
        conf.put("channel.capacity", String.valueOf(channel.getLocalCacheCapacity()));
        conf.put("channel.minimumRequiredSpace", String.valueOf(channel.getLocalCacheMinStorageSpace()));
        conf.put("channel.maxFileSize", channel.getLocalCacheMaxFileSize());
        if (StringUtils.isBlank(channel.getLocalCacheDir())) {
            throw new FlumeException(this.getName() + " local_cache_dir is empty!");
        }
        if (channel.getLocalCacheDataDirNum() < 4) {
            throw new FlumeException(this.getName() + " local_cache_data_dir_num < 4 !");
        }
        String[] dataDirArr = new String[channel.getLocalCacheDataDirNum()];

        for (int i = 0; i < channel.getLocalCacheDataDirNum(); ++i) {
            dataDirArr[i] = channel.getLocalCacheDir() + '/' + "data" + i;
        }

        String dataDirs = StringUtils.join(dataDirArr, ',');
        conf.put("channel.dataDirs", dataDirs);
        String checkpointDir = channel.getLocalCacheDir() + '/' + "checkpoint";
        conf.put("channel.checkpointDir", checkpointDir);
        conf.put("channel.checkpointInterval", String.valueOf(channel.getLocalCacheCheckpointInterval()));
        if (channel.isLocalCacheBackupCheckpoint()) {
            conf.put("channel.useDualCheckpoints", String.valueOf(true));
            String backupCheckpointDir = channel.getLocalCacheDir() + '/' + "backupCheckpoint";
            conf.put("channel.backupCheckpointDir", backupCheckpointDir);
        }

        if (!channel.isLocalCacheCheckpointOnClose()) {
            conf.put("channel.checkpointOnClose", String.valueOf(channel.isLocalCacheCheckpointOnClose()));
        }

        conf.put("channel.transactionCapacity", String.valueOf(channel.getLocalCacheTransactionCapacity()));
        conf.put("channel.keep-alive", String.valueOf(channel.getLocalCacheRequestTimeout()));
        Map<String, String> specificConf = this.collectSpecificChannelConf();
        if (specificConf != null) {
            conf.putAll(specificConf);
        }

        return conf;
    }

    private Map<String, String> getCollectSinkConf(FlumeSink sink) {
        Map<String, String> conf = new HashMap<>(8);
        String[] serverArr = StringUtils.split(sink.getServers(), ',');
        int totalServer = serverArr.length;
        String[] sinkArr = new String[totalServer];

        for (int i = 0; i < totalServer; ++i) {
            String[] ap = StringUtils.split(serverArr[i], ':');
            String a = ap[0];
            String p = ap[1];
            String sinkName = i + "sink" + StringUtils.replaceChars(a, '.', '_');
            sinkArr[i] = sinkName;
            conf.put(sinkName + ".type", "avro");
            conf.put(sinkName + ".hostname", a);
            conf.put(sinkName + ".port", p);
            conf.put(sinkName + ".batch-size", String.valueOf(sink.getServersTransactionCapacity()));
            conf.put(sinkName + ".maxIoWorkers", String.valueOf(sink.getServersMaxIoWorkers()));
            conf.put(sinkName + ".connect-timeout", String.valueOf(sink.getServersConnectTimeout()));
            conf.put(sinkName + ".request-timeout", String.valueOf(sink.getServersRequestTimeout()));
        }

        conf.put("sinks", StringUtils.join(sinkArr, ' '));
        if (totalServer == 1) {
            conf.put("processor.type", "failover");
            conf.put("processor.maxpenalty", String.valueOf(sink.getServersMaxBackoff()));
        } else {
            conf.put("processor.type", "load_balance");
            conf.put("processor.selector", sink.getServersLoadBalance());
            conf.put("processor.backoff", "true");
            conf.put("processor.selector.maxTimeOut", String.valueOf(sink.getServersMaxBackoff()));
        }

        Map<String, String> specificConf = this.collectSpecificSinkConf();
        if (specificConf != null) {
            conf.putAll(specificConf);
        }

        return conf;
    }

    /**
     * specific channel config
     * 
     * @return the specific config for channel
     */
    protected abstract Map<String, String> collectSpecificChannelConf();

    /**
     * specific sink config
     *
     * @return the specific config for sink
     */
    protected abstract Map<String, String> collectSpecificSinkConf();

}
