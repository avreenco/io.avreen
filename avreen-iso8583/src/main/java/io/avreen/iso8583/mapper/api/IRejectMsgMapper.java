package io.avreen.iso8583.mapper.api;

import java.nio.ByteBuffer;

/**
 * The interface Reject msg mapper.
 */
public interface IRejectMsgMapper {
    /**
     * Pack int.
     *
     * @param rejectBuffer the reject buffer
     * @param byteBuffer   the byte buffer
     * @return the int
     * @throws Exception the exception
     */
    int write(byte[] rejectBuffer, ByteBuffer byteBuffer) throws Exception;
}

