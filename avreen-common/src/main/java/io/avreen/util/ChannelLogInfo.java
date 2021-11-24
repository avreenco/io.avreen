package io.avreen.util;

import java.net.SocketAddress;

/**
 * The class Channel log info.
 */
public class ChannelLogInfo {
    private SocketAddress remoteAddress;
    private SocketAddress localAddress;
    private String        sessionId;

    /**
     * Instantiates a new Channel log info.
     */
    public ChannelLogInfo() {
    }

    /**
     * Instantiates a new Channel log info.
     *
     * @param remoteAddress the remote address
     * @param localAddress  the local address
     */
    public ChannelLogInfo(SocketAddress remoteAddress, SocketAddress localAddress , String   sessionId) {
        this.remoteAddress = remoteAddress;
        this.localAddress = localAddress;
        this.sessionId = sessionId;
    }

    /**
     * Gets local address.
     *
     * @return the local address
     */
    public SocketAddress getLocalAddress() {
        return localAddress;
    }

    /**
     * Sets local address.
     *
     * @param localAddress the local address
     */
    public void setLocalAddress(SocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    /**
     * Gets remote address.
     *
     * @return the remote address
     */
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Sets remote address.
     *
     * @param remoteAddress the remote address
     */
    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public ChannelLogInfo setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
}
