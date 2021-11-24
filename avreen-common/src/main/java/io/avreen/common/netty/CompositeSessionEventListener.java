package io.avreen.common.netty;


import io.netty.channel.Channel;

import java.net.SocketAddress;
import java.util.List;

/**
 * The class Composite session event listener.
 */
public class CompositeSessionEventListener implements ISessionEventListener {
    private List<ISessionEventListener> listeners;

    /**
     * Instantiates a new Composite session event listener.
     */
    public CompositeSessionEventListener() {
    }

    /**
     * Instantiates a new Composite session event listener.
     *
     * @param listeners the listeners
     */
    public CompositeSessionEventListener(List<ISessionEventListener> listeners) {
        this.listeners = listeners;
    }

    /**
     * Gets listeners.
     *
     * @return the listeners
     */
    public List<ISessionEventListener> getListeners() {
        return listeners;
    }

    /**
     * Sets listeners.
     *
     * @param listeners the listeners
     */
    public void setListeners(List<ISessionEventListener> listeners) {
        this.listeners = listeners;
    }


    @Override
    public void fire(ChannelEventTypes eventName, Channel channel, Throwable e) {
        if (listeners == null)
            return;
        for (ISessionEventListener listener : listeners) {
            listener.fire(eventName, channel, e);
        }

    }

    @Override
    public void fire(ChannelEventTypes eventName, SocketAddress remoteAddress, SocketAddress localAddress, Throwable e) {
        if (listeners == null)
            return;
        for (ISessionEventListener listener : listeners) {
            listener.fire(eventName, remoteAddress, localAddress, e);
        }

    }
}
