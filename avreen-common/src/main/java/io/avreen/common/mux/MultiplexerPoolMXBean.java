package io.avreen.common.mux;

import io.avreen.common.actor.ActorBaseMXBean;

/**
 * The interface Multiplexer pool mx bean.
 */
public interface MultiplexerPoolMXBean extends ActorBaseMXBean {
    /**
     * Get muxs as string string [ ].
     *
     * @return the string [ ]
     */
    String[] getMUXSAsString();
}