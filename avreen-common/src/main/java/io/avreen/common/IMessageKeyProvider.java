package io.avreen.common;

/**
 * The interface Message key provider.
 *
 * @param <T> the type parameter
 */
public interface IMessageKeyProvider<T> {
    /**
     * Gets key.
     *
     * @param m        the m
     * @param muxName  the mux name
     * @param outgoing the outgoing
     * @return the key
     */
    String getKey(T m, String muxName, boolean outgoing);
}
