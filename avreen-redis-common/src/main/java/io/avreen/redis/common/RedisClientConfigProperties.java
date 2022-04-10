package io.avreen.redis.common;

import io.lettuce.core.protocol.ProtocolVersion;

import java.time.Duration;
import java.util.ArrayList;

/**
 * The class Redis client config properties.
 */
public class RedisClientConfigProperties {
    private String name;
    private String redisUri = "redis://localhost:6379";
    private boolean clusterMode = false;
    private ArrayList<String> redisClusterUriList = new ArrayList<>();


    private Duration connectTimeout;
    private Boolean keepAlive;
    private Boolean tcpNoDelay;
    private Boolean autoReconnect;
    private ProtocolVersion   protocolVersion;

    /**
     * Gets redis uri.
     *
     * @return the redis uri
     */
    public String getRedisUri() {
        return redisUri;
    }

    /**
     * Sets redis uri.
     *
     * @param redisUri the redis uri
     */
    public void setRedisUri(String redisUri) {
        this.redisUri = redisUri;
    }

    /**
     * Gets connect timeout.
     *
     * @return the connect timeout
     */
    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Sets connect timeout.
     *
     * @param connectTimeout the connect timeout
     */
    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Gets keep alive.
     *
     * @return the keep alive
     */
    public Boolean getKeepAlive() {
        return keepAlive;
    }

    /**
     * Sets keep alive.
     *
     * @param keepAlive the keep alive
     */
    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    /**
     * Gets tcp no delay.
     *
     * @return the tcp no delay
     */
    public Boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    /**
     * Sets tcp no delay.
     *
     * @param tcpNoDelay the tcp no delay
     */
    public void setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    /**
     * Gets auto reconnect.
     *
     * @return the auto reconnect
     */
    public Boolean getAutoReconnect() {
        return autoReconnect;
    }

    /**
     * Sets auto reconnect.
     *
     * @param autoReconnect the auto reconnect
     */
    public void setAutoReconnect(Boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    /**
     * Is cluster mode boolean.
     *
     * @return the boolean
     */
    public boolean isClusterMode() {
        return clusterMode;
    }

    /**
     * Sets cluster mode.
     *
     * @param clusterMode the cluster mode
     * @return the cluster mode
     */
    public RedisClientConfigProperties setClusterMode(boolean clusterMode) {
        this.clusterMode = clusterMode;
        return this;
    }

    /**
     * Gets redis cluster uri list.
     *
     * @return the redis cluster uri list
     */
    public ArrayList<String> getRedisClusterUriList() {
        return redisClusterUriList;
    }

    /**
     * Sets redis cluster uri list.
     *
     * @param redisClusterUriList the redis cluster uri list
     * @return the redis cluster uri list
     */
    public RedisClientConfigProperties setRedisClusterUriList(ArrayList<String> redisClusterUriList) {
        this.redisClusterUriList = redisClusterUriList;
        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
