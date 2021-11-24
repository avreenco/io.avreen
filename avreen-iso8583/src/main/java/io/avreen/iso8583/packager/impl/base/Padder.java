package io.avreen.iso8583.packager.impl.base;

/**
 * The interface Padder.
 *
 * @param <T> the type parameter
 */
public interface Padder<T> {
    /**
     * Pad t.
     *
     * @param data      the data
     * @param maxLength the max length
     * @return the t
     */
    T pad(T data, int maxLength);
}