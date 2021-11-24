package io.avreen.common.netty;

import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;

import java.time.Duration;

/**
 * The class Traffic configuration properties.
 */
public class TrafficConfigurationProperties {

    private long totalChannelThroughput;
    private long perChannelThroughput;
    private Duration throughputCheckInterval = Duration.ofMillis(GlobalChannelTrafficShapingHandler.DEFAULT_CHECK_INTERVAL);
    private Duration trafficCheckMaxTime = Duration.ofMillis(GlobalChannelTrafficShapingHandler.DEFAULT_MAX_TIME);

    /**
     * Gets total channel throughput.
     *
     * @return the total channel throughput
     */
    public long getTotalChannelThroughput() {
        return totalChannelThroughput;
    }

    /**
     * Sets total channel throughput.
     *
     * @param totalChannelThroughput the total channel throughput
     * @return the total channel throughput
     */
    public TrafficConfigurationProperties setTotalChannelThroughput(long totalChannelThroughput) {
        this.totalChannelThroughput = totalChannelThroughput;
        return this;
    }

    /**
     * Gets per channel throughput.
     *
     * @return the per channel throughput
     */
    public long getPerChannelThroughput() {
        return perChannelThroughput;
    }

    /**
     * Sets per channel throughput.
     *
     * @param perChannelThroughput the per channel throughput
     * @return the per channel throughput
     */
    public TrafficConfigurationProperties setPerChannelThroughput(long perChannelThroughput) {
        this.perChannelThroughput = perChannelThroughput;
        return this;
    }

    /**
     * Gets throughput check interval.
     *
     * @return the throughput check interval
     */
    public Duration getThroughputCheckInterval() {
        return throughputCheckInterval;
    }

    /**
     * Sets throughput check interval.
     *
     * @param throughputCheckInterval the throughput check interval
     * @return the throughput check interval
     */
    public TrafficConfigurationProperties setThroughputCheckInterval(Duration throughputCheckInterval) {
        this.throughputCheckInterval = throughputCheckInterval;
        return this;
    }

    /**
     * Gets traffic check max time.
     *
     * @return the traffic check max time
     */
    public Duration getTrafficCheckMaxTime() {
        return trafficCheckMaxTime;
    }

    /**
     * Sets traffic check max time.
     *
     * @param trafficCheckMaxTime the traffic check max time
     * @return the traffic check max time
     */
    public TrafficConfigurationProperties setTrafficCheckMaxTime(Duration trafficCheckMaxTime) {
        this.trafficCheckMaxTime = trafficCheckMaxTime;
        return this;
    }
}
