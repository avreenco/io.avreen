package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.api.ISOMsgPackager;
import io.avreen.iso8583.packager.impl.base.ISOMsgBasePackager;

import java.nio.ByteBuffer;

/**
 * The class Passthrough iso msg packager.
 */
public class PassthroughISOMsgPackager extends ISOMsgBasePackager {
    private ISOMsgPackager isoMsgPackager;

    /**
     * Instantiates a new Passthrough iso msg packager.
     *
     * @param isoMsgPackager the iso msg packager
     */
    public PassthroughISOMsgPackager(ISOMsgPackager isoMsgPackager) {
        this.isoMsgPackager = isoMsgPackager;
    }

    @Override
    public int pack(ISOMsg m, ByteBuffer byteBuffer) {
        if (m.getRawBuffer() != null) {
            byte[] allBytes = m.getRawBuffer();
            byteBuffer.put(allBytes);
            return allBytes.length;
        } else
            return isoMsgPackager.pack(m, byteBuffer);
    }

    @Override
    public int unpack(ISOMsg m, ByteBuffer byteBuffer) {
        return isoMsgPackager.unpack(m, byteBuffer);
    }

    @Override
    public String toString() {
        return "Passthrough*" + isoMsgPackager.toString();
    }
}
