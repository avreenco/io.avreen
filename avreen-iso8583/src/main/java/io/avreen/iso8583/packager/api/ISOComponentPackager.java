package io.avreen.iso8583.packager.api;

import io.avreen.iso8583.common.ISOComponent;

import java.nio.ByteBuffer;

/**
 * The interface Iso component packager.
 *
 * @param <C> the type parameter
 */
public interface ISOComponentPackager<C extends ISOComponent> {

    /**
     * Create component c.
     *
     * @return the c
     */
    C createComponent();

    /**
     * Pack int.
     *
     * @param isoComponent the iso component
     * @param byteBuffer   the byte buffer
     * @return the int
     */
    int pack(C isoComponent, ByteBuffer byteBuffer);

    /**
     * Unpack int.
     *
     * @param isoComponent the iso component
     * @param byteBuffer   the byte buffer
     * @return the int
     */
    int unpack(C isoComponent, ByteBuffer byteBuffer);


}

