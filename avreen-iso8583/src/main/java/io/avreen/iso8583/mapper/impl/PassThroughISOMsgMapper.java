package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.mapper.api.ISOMsgMapper;
import io.avreen.iso8583.mapper.impl.base.ISOMsgBaseMapper;

import java.nio.ByteBuffer;

/**
 * The class Passthrough iso msg mapper.
 */
public class PassThroughISOMsgMapper extends ISOMsgBaseMapper {
    private ISOMsgMapper isoMsgMapper;

    /**
     * Instantiates a new Passthrough iso msg mapper.
     *
     * @param isoMsgMapper the iso msg mapper
     */
    public PassThroughISOMsgMapper(ISOMsgMapper isoMsgMapper) {
        this.isoMsgMapper = isoMsgMapper;
    }

    @Override
    public void write(ISOMsg m, ByteBuffer byteBuffer) {
        if (m.getRawBuffer() != null) {
            byte[] allBytes = m.getRawBuffer();
            byteBuffer.put(allBytes);
        } else
             isoMsgMapper.write(m, byteBuffer);
    }

    @Override
    public ISOMsg read(ByteBuffer byteBuffer) {
         return isoMsgMapper.read(byteBuffer);
    }

    @Override
    public String toString() {
        return "Passthrough*" + isoMsgMapper.toString();
    }
}
