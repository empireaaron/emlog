package com.empire.emlog.agent.flume.log4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.flume.FlumeException;

/**
 * @author aaron.xu
 */
public abstract class AbstractConfiguration {
    private Properties props;
    private Properties defaultProps;
    private Properties original;

    Map<String, String> configure(Properties props, Properties defaultProps) {
        this.props = props;
        this.defaultProps = defaultProps;
        return this.doConfigure();
    }

    private Map<String, String> doConfigure() {
        initConfigure(props, defaultProps);

        Map<String, String> conf = new HashMap<>(8);
        conf.putAll(this.getCollectChannelConf(original));
        conf.putAll(this.getCollectSinkConf(original));
        conf.put("agent.name", original.getProperty("agent.name"));
        conf.put("lcd", original.getProperty("channel.local_cache_dir"));
        return conf;
    }

    private void initConfigure(Properties props, Properties defaultProps) {
        Properties original = new Properties();
        for (Map.Entry entry : defaultProps.entrySet()) {
            original.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry entry : props.entrySet()) {
            if (entry.getValue() != null && !"".equals(entry.getValue())) {
                original.put(entry.getKey(), entry.getValue());
            }
        }
        this.original = original;
    }

    private Map<String, String> getCollectChannelConf(Properties original) {
        Map<String, String> conf = new HashMap<>(8);
        conf.put("channel.type", "file");
        conf.put("channel.capacity", original.getProperty("channel.local_cache_capacity"));
        conf.put("channel.minimumRequiredSpace", original.getProperty("channel.local_cache_min_storage_space"));
        conf.put("channel.maxFileSize", original.getProperty("channel.local_cache_max_file_size"));
        if (original.getProperty("channel.local_cache_dir") == null
            || "".equals(original.getProperty("channel.local_cache_dir"))) {
            throw new FlumeException(original.getProperty("agent.name") + " local_cache_dir is empty!");
        }
        //
        Integer lcdNum = Integer.parseInt(original.getProperty("channel.local_cache_data_dir_num"));

        if (lcdNum < 4) {
            throw new FlumeException(original.getProperty("agent.name") + " local_cache_data_dir_num < 4 !");
        }
        String[] dataDirArr = new String[lcdNum];

        for (int i = 0; i < lcdNum; ++i) {
            dataDirArr[i] = original.getProperty("channel.local_cache_dir") + '/' + "data" + i;
        }

        String dataDirs = StringUtils.join(dataDirArr, ',');
        conf.put("channel.dataDirs", dataDirs);
        String checkpointDir = original.getProperty("channel.local_cache_dir") + '/' + "checkpoint";
        conf.put("channel.checkpointDir", checkpointDir);
        conf.put("channel.checkpointInterval", original.getProperty("channel.local_cache_checkpoint_interval"));
        if (Boolean.valueOf(original.getProperty("channel.local_cache_backup_checkpoint"))) {
            conf.put("channel.useDualCheckpoints", String.valueOf(true));
            String backupCheckpointDir = original.getProperty("channel.local_cache_dir") + '/' + "backupCheckpoint";
            conf.put("channel.backupCheckpointDir", backupCheckpointDir);
        }

        if (!Boolean.valueOf(original.getProperty("channel.local_cache_checkpoint_on_close"))) {
            conf.put("channel.checkpointOnClose", original.getProperty("channel.local_cache_checkpoint_on_close"));
        }

        conf.put("channel.transactionCapacity", original.getProperty("channel.local_cache_transaction_capacity"));
        conf.put("channel.keep-alive", original.getProperty("channel.local_cache_request_timeout"));
        Map<String, String> specificConf = this.collectSpecificChannelConf();
        if (specificConf != null) {
            conf.putAll(specificConf);
        }

        return conf;
    }

    private Map<String, String> getCollectSinkConf(Properties original) {
        Map<String, String> conf = new HashMap<>(8);
        String[] serverArr = StringUtils.split(original.getProperty("sink.servers"), ',');
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
            conf.put(sinkName + ".batch-size", original.getProperty("sink.servers_transaction_capacity"));
            conf.put(sinkName + ".maxIoWorkers", original.getProperty("sink.servers_max_io_workers"));
            conf.put(sinkName + ".connect-timeout", original.getProperty("sink.servers_connect_timeout"));
            conf.put(sinkName + ".request-timeout", original.getProperty("sink.servers_request_timeout"));
        }

        conf.put("sinks", StringUtils.join(sinkArr, ' '));
        if (totalServer == 1) {
            conf.put("processor.type", "failover");
            conf.put("processor.maxpenalty", original.getProperty("sink.servers_max_backoff"));
        } else {
            conf.put("processor.type", "load_balance");
            conf.put("processor.selector", original.getProperty("sink.servers_load_balance"));
            conf.put("processor.backoff", "true");
            conf.put("processor.selector.maxTimeOut", original.getProperty("sink.servers_max_backoff"));
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
