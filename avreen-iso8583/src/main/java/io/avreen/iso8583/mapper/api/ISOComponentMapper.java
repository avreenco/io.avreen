package io.avreen.iso8583.mapper.api;

import io.avreen.iso8583.common.ISOComponent;

import java.nio.ByteBuffer;

/**
 * The interface Iso component mapper.
 *
 * @param <C> the type parameter
 */
public interface ISOComponentMapper<C extends ISOComponent> {

    /**
     * Pack int.
     *
     * @param isoComponent the iso component
     * @param byteBuffer   the byte buffer
     */
    void write(C isoComponent, ByteBuffer byteBuffer);

    /**
     * Unpack int.
     *
     * @param byteBuffer   the byte buffer
     * @return the int
     */
    C read(ByteBuffer byteBuffer);


}

