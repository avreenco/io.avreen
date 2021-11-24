package io.avreen.iso8583.packager.api;

import java.nio.ByteBuffer;

/**
 * The interface Reject msg packager.
 */
public interface IRejectMsgPackager {
    /**
     * Pack int.
     *
     * @param rejectBuffer the reject buffer
     * @param byteBuffer   the byte buffer
     * @return the int
     * @throws Exception the exception
     */
    int pack(byte[] rejectBuffer, ByteBuffer byteBuffer) throws Exception;
}

