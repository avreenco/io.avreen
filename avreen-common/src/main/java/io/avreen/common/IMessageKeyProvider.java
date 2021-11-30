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
     * @param ownerName  the mux name
     * @param outgoing the outgoing
     * @return the key
     */
    String getKey(T m, String ownerName, boolean outgoing);
}
