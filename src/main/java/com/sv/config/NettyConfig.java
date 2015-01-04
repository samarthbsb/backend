package com.sv.config;

public class NettyConfig {

    // port to listen and accept connections on
    private int    httpport;
    // hostname of the broker. If not set, we pick it up from the value returned from getLocalHost
    private String hostName;
    private String hostAddr;
    // the broker id for this server
    private int    brokerId;
    // number of worker threads that the server uses for handling all client requests
    private int    numThreads;
    private int    corePoolSize;
    private int    maxPoolSize;
    // the interval in which to measure performance stats in seconds
    private int    monitoringStatsTime;

    public int getHttpport() {
        return httpport;
    }

    public void setHttpport(int httpport) {
        this.httpport = httpport;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAddr() {
        return hostAddr;
    }

    public void setHostAddr(String hostAddr) {
        this.hostAddr = hostAddr;
    }

    public int getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(int brokerId) {
        this.brokerId = brokerId;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMonitoringStatsTime() {
        return monitoringStatsTime;
    }

    public void setMonitoringStatsTime(int monitoringStatsTime) {
        this.monitoringStatsTime = monitoringStatsTime;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SVConfig : ");
        buffer.append(" HTTP Port : " + getHttpport());
        buffer.append(']');
        return buffer.toString();

    }
}
