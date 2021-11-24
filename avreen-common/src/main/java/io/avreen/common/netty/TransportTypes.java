package io.avreen.common.netty;

/**
 * The enum Transport types.
 */
public enum TransportTypes {
    /**
     * Nio transport types.
     */
    nio,
    /**
     * Epoll transport types.
     */
    epoll,
    /**
     * Kqueue transport types.
     */
    kqueue,
    /**
     * Io uring transport types.
     */
    io_uring;
}
