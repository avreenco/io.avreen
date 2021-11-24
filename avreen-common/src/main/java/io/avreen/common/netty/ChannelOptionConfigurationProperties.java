package io.avreen.common.netty;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.SocketChannelConfig;

/**
 * The class Channel option configuration properties.
 */
public class ChannelOptionConfigurationProperties {
    private Boolean tcpNoDelay;
    private Integer soLinger;
    private Integer sendBufferSize;
    private Integer receiveBufferSize;
    private Boolean keepAlive;
    private Boolean reuseAddress;
    private Boolean allowHalfClosure;
    private Integer connectTimeoutMillis;
    private Integer backLog;
    private Integer writeSpinCount;
    private Boolean autoRead;
    private RecvByteBufAllocator recvByteBufAllocator;
    private ByteBufAllocator byteBufAllocator;

    private static void applyCommonDefault(ChannelOptionConfigurationProperties channelOptionConfiguration) {
        if (channelOptionConfiguration.getSendBufferSize() == null)
            channelOptionConfiguration.setSendBufferSize(65535);
        if (channelOptionConfiguration.getReceiveBufferSize() == null)
            channelOptionConfiguration.setReceiveBufferSize(65535);
        if (channelOptionConfiguration.getRecvByteBufAllocator() == null)
            channelOptionConfiguration.setRecvByteBufAllocator(new AdaptiveRecvByteBufAllocator());
        if (channelOptionConfiguration.getTcpNoDelay() == null)
            channelOptionConfiguration.setTcpNoDelay(true);
        return;
    }

    /**
     * Apply server default.
     *
     * @param channelOptionConfiguration the channel option configuration
     */
    public static void applyServerDefault(ChannelOptionConfigurationProperties channelOptionConfiguration) {
        applyCommonDefault(channelOptionConfiguration);
        return;
    }

    /**
     * Apply client default.
     *
     * @param channelOptionConfiguration the channel option configuration
     */
    public static void applyClientDefault(ChannelOptionConfigurationProperties channelOptionConfiguration) {

        applyCommonDefault(channelOptionConfiguration);
    }

    /**
     * Gets byte buf allocator.
     *
     * @return the byte buf allocator
     */
    public ByteBufAllocator getByteBufAllocator() {
        return byteBufAllocator;
    }

    /**
     * Sets byte buf allocator.
     *
     * @param byteBufAllocator the byte buf allocator
     */
    public void setByteBufAllocator(ByteBufAllocator byteBufAllocator) {
        this.byteBufAllocator = byteBufAllocator;
    }

    /**
     * Map channel options.
     *
     * @param socketChannelConfig the socket channel config
     */
    public void mapChannelOptions(SocketChannelConfig socketChannelConfig) {
        if (tcpNoDelay != null)
            socketChannelConfig.setTcpNoDelay(tcpNoDelay);
        if (soLinger != null)
            socketChannelConfig.setSoLinger(soLinger);
        if (sendBufferSize != null)
            socketChannelConfig.setSendBufferSize(sendBufferSize);
        if (receiveBufferSize != null)
            socketChannelConfig.setReceiveBufferSize(receiveBufferSize);
        if (keepAlive != null)
            socketChannelConfig.setKeepAlive(keepAlive);
        if (reuseAddress != null)
            socketChannelConfig.setReuseAddress(reuseAddress);
        if (allowHalfClosure != null)
            socketChannelConfig.setAllowHalfClosure(allowHalfClosure);
        if (connectTimeoutMillis != null)
            socketChannelConfig.setConnectTimeoutMillis(connectTimeoutMillis);
        if (writeSpinCount != null)
            socketChannelConfig.setWriteSpinCount(writeSpinCount);
        if (autoRead != null)
            socketChannelConfig.setAutoRead(autoRead);
        if (recvByteBufAllocator != null)
            socketChannelConfig.setRecvByteBufAllocator(recvByteBufAllocator);
        else
            socketChannelConfig.setRecvByteBufAllocator(new AdaptiveRecvByteBufAllocator());
        if (byteBufAllocator != null)
            socketChannelConfig.setAllocator(byteBufAllocator);
        if (backLog != null)
            socketChannelConfig.setOption(ChannelOption.SO_BACKLOG, backLog);
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
     * @return the tcp no delay
     */
    public ChannelOptionConfigurationProperties setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
        return this;
    }

    /**
     * Gets so linger.
     *
     * @return the so linger
     */
    public Integer getSoLinger() {
        return soLinger;
    }

    /**
     * Sets so linger.
     *
     * @param soLinger the so linger
     * @return the so linger
     */
    public ChannelOptionConfigurationProperties setSoLinger(Integer soLinger) {
        this.soLinger = soLinger;
        return this;
    }

    /**
     * Gets send buffer size.
     *
     * @return the send buffer size
     */
    public Integer getSendBufferSize() {
        return sendBufferSize;
    }

    /**
     * Sets send buffer size.
     *
     * @param sendBufferSize the send buffer size
     * @return the send buffer size
     */
    public ChannelOptionConfigurationProperties setSendBufferSize(Integer sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
        return this;
    }

    /**
     * Gets receive buffer size.
     *
     * @return the receive buffer size
     */
    public Integer getReceiveBufferSize() {
        return receiveBufferSize;
    }

    /**
     * Sets receive buffer size.
     *
     * @param receiveBufferSize the receive buffer size
     * @return the receive buffer size
     */
    public ChannelOptionConfigurationProperties setReceiveBufferSize(Integer receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
        return this;
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
     * @return the keep alive
     */
    public ChannelOptionConfigurationProperties setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    /**
     * Gets reuse address.
     *
     * @return the reuse address
     */
    public Boolean getReuseAddress() {
        return reuseAddress;
    }

    /**
     * Sets reuse address.
     *
     * @param reuseAddress the reuse address
     * @return the reuse address
     */
    public ChannelOptionConfigurationProperties setReuseAddress(Boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
        return this;
    }

    /**
     * Gets allow half closure.
     *
     * @return the allow half closure
     */
    public Boolean getAllowHalfClosure() {
        return allowHalfClosure;
    }

    /**
     * Sets allow half closure.
     *
     * @param allowHalfClosure the allow half closure
     * @return the allow half closure
     */
    public ChannelOptionConfigurationProperties setAllowHalfClosure(Boolean allowHalfClosure) {
        this.allowHalfClosure = allowHalfClosure;
        return this;
    }

    /**
     * Gets connect timeout millis.
     *
     * @return the connect timeout millis
     */
    public Integer getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    /**
     * Sets connect timeout millis.
     *
     * @param connectTimeoutMillis the connect timeout millis
     * @return the connect timeout millis
     */
    public ChannelOptionConfigurationProperties setConnectTimeoutMillis(Integer connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }

    /**
     * Gets write spin count.
     *
     * @return the write spin count
     */
    public Integer getWriteSpinCount() {
        return writeSpinCount;
    }

    /**
     * Sets write spin count.
     *
     * @param writeSpinCount the write spin count
     * @return the write spin count
     */
    public ChannelOptionConfigurationProperties setWriteSpinCount(Integer writeSpinCount) {
        this.writeSpinCount = writeSpinCount;
        return this;
    }

    /**
     * Gets auto read.
     *
     * @return the auto read
     */
    public Boolean getAutoRead() {
        return autoRead;
    }

    /**
     * Sets auto read.
     *
     * @param autoRead the auto read
     * @return the auto read
     */
    public ChannelOptionConfigurationProperties setAutoRead(Boolean autoRead) {
        this.autoRead = autoRead;
        return this;
    }

    /**
     * Gets recv byte buf allocator.
     *
     * @return the recv byte buf allocator
     */
    public RecvByteBufAllocator getRecvByteBufAllocator() {
        return recvByteBufAllocator;
    }

    /**
     * Sets recv byte buf allocator.
     *
     * @param recvByteBufAllocator the recv byte buf allocator
     * @return the recv byte buf allocator
     */
    public ChannelOptionConfigurationProperties setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        this.recvByteBufAllocator = recvByteBufAllocator;
        return this;
    }

    /**
     * Gets back log.
     *
     * @return the back log
     */
    public Integer getBackLog() {
        return backLog;
    }

    /**
     * Sets back log.
     *
     * @param backLog the back log
     * @return the back log
     */
    public ChannelOptionConfigurationProperties setBackLog(Integer backLog) {
        this.backLog = backLog;
        return this;
    }
}
