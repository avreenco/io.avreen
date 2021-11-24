package io.avreen.common.netty;

/**
 * The enum Channel event types.
 */
public enum ChannelEventTypes {
    /**
     * Handler added channel event types.
     */
    handlerAdded,
    /**
     * Handler removed channel event types.
     */
    handlerRemoved,
    /**
     * Channel active channel event types.
     */
    channelActive,
    /**
     * Channel inactive channel event types.
     */
    channelInactive,
    /**
     * Connect channel event types.
     */
    connect,
    /**
     * Channel registered channel event types.
     */
    channelRegistered,
    /**
     * Bind channel event types.
     */
    bind,
    /**
     * Disconnect channel event types.
     */
    disconnect,
    /**
     * Close channel event types.
     */
    close,
    /**
     * Deregister channel event types.
     */
    deregister,
    /**
     * Channel unregistered channel event types.
     */
    channelUnregistered,
    /**
     * Exception caught channel event types.
     */
    exceptionCaught
}
