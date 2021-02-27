package com.empire.emlog.agent.flume.logback;

/**
 * local flume agent channel config
 * 
 * @author aaron.xu
 */
public class FlumeChannel {
    private String localCacheDir;
    private int localCacheCapacity = 10000000;
    private int localCacheMinStorageSpace = 524288000;
    private String localCacheMaxFileSize = "134217728";

    private int localCacheDataDirNum = 4;

    private long localCacheCheckpointInterval = 30000L;
    private boolean localCacheBackupCheckpoint = true;
    private boolean localCacheCheckpointOnClose = false;
    private int localCacheTransactionCapacity = 6000;
    private int localCacheRequestTimeout = 10;

    FlumeChannel() {}

    public FlumeChannel(String localCacheDir) {
        this.localCacheDir = localCacheDir;
    }

    @Override
    public String toString() {
        return "FlumeChannel{" + "localCacheDir='" + localCacheDir + '\'' + ", localCacheCapacity=" + localCacheCapacity
            + ", localCacheMinStorageSpace=" + localCacheMinStorageSpace + ", localCacheMaxFileSize='"
            + localCacheMaxFileSize + '\'' + ", localCacheDataDirNum=" + localCacheDataDirNum
            + ", localCacheCheckpointInterval=" + localCacheCheckpointInterval + ", localCacheBackupCheckpoint="
            + localCacheBackupCheckpoint + ", localCacheCheckpointOnClose=" + localCacheCheckpointOnClose
            + ", localCacheTransactionCapacity=" + localCacheTransactionCapacity + ", localCacheRequestTimeout="
            + localCacheRequestTimeout + '}';
    }

    String getLocalCacheDir() {
        return localCacheDir;
    }

    void setLocalCacheDir(String localCacheDir) {
        this.localCacheDir = localCacheDir;
    }

    int getLocalCacheCapacity() {
        return localCacheCapacity;
    }

    public void setLocalCacheCapacity(int localCacheCapacity) {
        this.localCacheCapacity = localCacheCapacity;
    }

    int getLocalCacheMinStorageSpace() {
        return localCacheMinStorageSpace;
    }

    public void setLocalCacheMinStorageSpace(int localCacheMinStorageSpace) {
        this.localCacheMinStorageSpace = localCacheMinStorageSpace;
    }

    String getLocalCacheMaxFileSize() {
        return localCacheMaxFileSize;
    }

    public void setLocalCacheMaxFileSize(String localCacheMaxFileSize) {
        this.localCacheMaxFileSize = localCacheMaxFileSize;
    }

    int getLocalCacheDataDirNum() {
        return localCacheDataDirNum;
    }

    public void setLocalCacheDataDirNum(int localCacheDataDirNum) {
        this.localCacheDataDirNum = localCacheDataDirNum;
    }

    long getLocalCacheCheckpointInterval() {
        return localCacheCheckpointInterval;
    }

    public void setLocalCacheCheckpointInterval(long localCacheCheckpointInterval) {
        this.localCacheCheckpointInterval = localCacheCheckpointInterval;
    }

    boolean isLocalCacheBackupCheckpoint() {
        return localCacheBackupCheckpoint;
    }

    public void setLocalCacheBackupCheckpoint(boolean localCacheBackupCheckpoint) {
        this.localCacheBackupCheckpoint = localCacheBackupCheckpoint;
    }

    boolean isLocalCacheCheckpointOnClose() {
        return localCacheCheckpointOnClose;
    }

    public void setLocalCacheCheckpointOnClose(boolean localCacheCheckpointOnClose) {
        this.localCacheCheckpointOnClose = localCacheCheckpointOnClose;
    }

    int getLocalCacheTransactionCapacity() {
        return localCacheTransactionCapacity;
    }

    public void setLocalCacheTransactionCapacity(int localCacheTransactionCapacity) {
        this.localCacheTransactionCapacity = localCacheTransactionCapacity;
    }

    int getLocalCacheRequestTimeout() {
        return localCacheRequestTimeout;
    }

    public void setLocalCacheRequestTimeout(int localCacheRequestTimeout) {
        this.localCacheRequestTimeout = localCacheRequestTimeout;
    }
}
