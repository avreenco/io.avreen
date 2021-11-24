package io.avreen.common.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;
import io.netty.incubator.channel.uring.IOUringSocketChannel;

/**
 * The class Channel config util.
 */
public class ChannelConfigUtil {

    /**
     * Create event group event loop group.
     *
     * @param transportTypes the transport types
     * @param nThreads       the n threads
     * @return the event loop group
     */
    public static EventLoopGroup createEventGroup(TransportTypes transportTypes, int nThreads) {
        if (TransportTypes.nio.equals(transportTypes))
            return new NioEventLoopGroup(nThreads);
        if (TransportTypes.epoll.equals(transportTypes))
            return new EpollEventLoopGroup(nThreads);
        if (TransportTypes.kqueue.equals(transportTypes))
            return new KQueueEventLoopGroup(nThreads);
        if (TransportTypes.io_uring.equals(transportTypes))
            return new IOUringEventLoopGroup(nThreads);
        throw new RuntimeException("can not create event loop group with channel type" + transportTypes);
    }

    /**
     * Gets server socket channel class.
     *
     * @param transportTypes the transport types
     * @return the server socket channel class
     */
    public static Class<? extends io.netty.channel.socket.ServerSocketChannel> getServerSocketChannelClass(TransportTypes transportTypes) {
        if (TransportTypes.nio.equals(transportTypes))
            return NioServerSocketChannel.class;
        if (TransportTypes.epoll.equals(transportTypes))
            return EpollServerSocketChannel.class;
        if (TransportTypes.kqueue.equals(transportTypes))
            return KQueueServerSocketChannel.class;
        if (TransportTypes.io_uring.equals(transportTypes))
            return IOUringServerSocketChannel.class;
        throw new RuntimeException("can not get server socket channel with channel type" + transportTypes);
    }

    /**
     * Gets socket channel class.
     *
     * @param transportTypes the transport types
     * @return the socket channel class
     */
    public static Class<? extends io.netty.channel.socket.SocketChannel> getSocketChannelClass(TransportTypes transportTypes) {
        if (TransportTypes.nio.equals(transportTypes))
            return NioSocketChannel.class;
        if (TransportTypes.epoll.equals(transportTypes))
            return EpollSocketChannel.class;
        if (TransportTypes.kqueue.equals(transportTypes))
            return KQueueSocketChannel.class;
        if (TransportTypes.io_uring.equals(transportTypes))
            return IOUringSocketChannel.class;
        throw new RuntimeException("can not get socket channel with channel type" + transportTypes);
    }

}
