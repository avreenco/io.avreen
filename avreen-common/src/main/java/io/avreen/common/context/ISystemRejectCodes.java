package io.avreen.common.context;

/**
 * The interface System reject codes.
 */
public interface ISystemRejectCodes {
    /**
     * The constant InvalidMsgLen.
     */
    int InvalidMsgLen = 9999;
    /**
     * The constant InvalidIsoMsg.
     */
    int InvalidIsoMsg = 9998;
    /**
     * The constant MTINotFound.
     */
    int ChannelCloseAbnormally = 9997;
    /**
     * The constant ChannelFirewallFailed.
     */
    int ChannelFirewallFailed = 9996;
    /**
     * The constant ProtocolFilterFailed.
     */
    int ProtocolFilterFailed = 9995;
    /**
     * The constant MessageExpired.
     */
    int MessageExpired = 9994;
    /**
     * The constant QueueFull.
     */
    int QueueFull = 9993;
    /**
     * The constant DestinationNotReady.
     */
    int DestinationNotReady = 9992;
    /**
     * The constant MessageRoutTimeout.
     */
    int MessageRoutTimeout = 9991;
}
