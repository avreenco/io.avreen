package io.avreen.hb;

import io.avreen.common.IMessageKeyProvider;
import io.avreen.common.util.TPDUUtil;

import java.nio.ByteBuffer;

/**
 * The class Tpdu multiplexer key provider.
 */
public class TPDUMultiplexerKeyProvider implements IMessageKeyProvider<HeaderBodyObject> {
    @Override
    public String getKey(HeaderBodyObject m, String outKey, boolean outgoing) {
        if (outgoing) {
            ByteBuffer wrapped = ByteBuffer.wrap(TPDUUtil.getSourceAddress(m.getHeader()));
            return ((Short) (wrapped.getShort())).toString();
        } else {
            ByteBuffer wrapped = ByteBuffer.wrap(TPDUUtil.getDestinationAddress(m.getHeader()));
            return ((Short) (wrapped.getShort())).toString();
        }
    }
}
