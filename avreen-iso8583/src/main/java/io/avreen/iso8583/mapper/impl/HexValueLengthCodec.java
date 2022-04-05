package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.mapper.impl.base.IValueLengthCodec;

import java.nio.ByteBuffer;

/**
 * The class hex value length codec
 */
public class HexValueLengthCodec implements IValueLengthCodec {
    /**
     * The constant B.
     */
    public static final HexValueLengthCodec L = new HexValueLengthCodec(1);
    /**
     * The constant BB.
     */
    public static final HexValueLengthCodec LL = new HexValueLengthCodec(2);
    private int nBytes;

    /**
     * Instantiates a new Binary ValueLengthCodec.
     *
     * @param nBytes the n bytes
     */
    private HexValueLengthCodec(int nBytes) {
        this.nBytes = nBytes;
    }

    public static HexValueLengthCodec of(int nDigits)
    {
        if(nDigits == 1)
            return HexValueLengthCodec.L;
        if(nDigits == 2)
            return HexValueLengthCodec.LL;
        return new HexValueLengthCodec(nDigits);
    }


    @Override
    public void encodeLength(int length, ByteBuffer b) {
        for (int i = nBytes - 1; i >= 0; i--) {
            b.put((byte) (length & 0xFF));
            length >>= 8;
        }
        return ;
    }

    @Override
    public int decodeLength(ByteBuffer b) {
        int len = 0;
        for (int i = 0; i < nBytes; i++) {
            len = 256 * len + (b.get() & 0xFF);
        }
        return len;
    }



    public int getPackedLength() {
        return nBytes;
    }
}