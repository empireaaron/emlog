package com.empire.emlog.agent.flume.logback;

/**
 * @author aaron.xu
 */
public class FlumeSink {
    private String servers;
    private String serversLoadBalance = "round_robin";
    private int serversMaxIoWorkers = 4;
    private int serversMaxBackoff = 6000;
    private int serversConnectTimeout = 8000;
    private int serversRequestTimeout = 8000;
    private int serversTransactionCapacity = 300;

    FlumeSink() {}

    public FlumeSink(String servers) {
        this.servers = servers;
    }

    void setServers(String servers) {
        this.servers = servers;
    }

    String getServers() {
        return servers;
    }

    @Override
    public String toString() {
        return "FlumeSink{" + "servers='" + servers + '\'' + ", serversLoadBalance='" + serversLoadBalance + '\''
            + ", serversMaxIoWorkers=" + serversMaxIoWorkers + ", serversMaxBackoff=" + serversMaxBackoff
            + ", serversConnectTimeout=" + serversConnectTimeout + ", serversRequestTimeout=" + serversRequestTimeout
            + ", serversTransactionCapacity=" + serversTransactionCapacity + '}';
    }

    String getServersLoadBalance() {
        return serversLoadBalance;
    }

    public void setServersLoadBalance(String serversLoadBalance) {
        this.serversLoadBalance = serversLoadBalance;
    }

    int getServersMaxIoWorkers() {
        return serversMaxIoWorkers;
    }

    public void setServersMaxIoWorkers(int serversMaxIoWorkers) {
        this.serversMaxIoWorkers = serversMaxIoWorkers;
    }

    int getServersMaxBackoff() {
        return serversMaxBackoff;
    }

    public void setServersMaxBackoff(int serversMaxBackoff) {
        this.serversMaxBackoff = serversMaxBackoff;
    }

    int getServersConnectTimeout() {
        return serversConnectTimeout;
    }

    public void setServersConnectTimeout(int serversConnectTimeout) {
        this.serversConnectTimeout = serversConnectTimeout;
    }

    int getServersRequestTimeout() {
        return serversRequestTimeout;
    }

    public void setServersRequestTimeout(int serversRequestTimeout) {
        this.serversRequestTimeout = serversRequestTimeout;
    }

    int getServersTransactionCapacity() {
        return serversTransactionCapacity;
    }

    public void setServersTransactionCapacity(int serversTransactionCapacity) {
        this.serversTransactionCapacity = serversTransactionCapacity;
    }
}
