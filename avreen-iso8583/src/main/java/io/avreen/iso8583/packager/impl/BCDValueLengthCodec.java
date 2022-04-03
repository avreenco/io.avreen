package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.impl.base.IValueLengthCodec;

import java.nio.ByteBuffer;

/**
 * The class bcd value length codec
 */
public class BCDValueLengthCodec implements IValueLengthCodec {
    /**
     * The constant L.
     */
    public static final BCDValueLengthCodec L = new BCDValueLengthCodec(1);
    /**
     * The constant LL.
     */
    public static final BCDValueLengthCodec LL = new BCDValueLengthCodec(2);
    /**
     * The constant LLL.
     */
    public static final BCDValueLengthCodec LLL = new BCDValueLengthCodec(3);
    /**
     * The constant LLLL.
     */
    public static final BCDValueLengthCodec LLLL = new BCDValueLengthCodec(4);
    private int nDigits;

    /**
     * Instantiates a new Bcd ValueLengthCodec.
     *
     * @param nDigits the n digits
     */
    private BCDValueLengthCodec(int nDigits) {
        this.nDigits = nDigits;
    }

    public static BCDValueLengthCodec of(int nDigits)
    {
        if(nDigits == 1)
            return BCDValueLengthCodec.L;
        if(nDigits == 2)
            return BCDValueLengthCodec.LL;
        if(nDigits == 3)
            return BCDValueLengthCodec.LLL;
        if(nDigits == 4)
            return BCDValueLengthCodec.LLLL;
        return new BCDValueLengthCodec(nDigits);
    }

    @Override
    public void encodeLength(int length, ByteBuffer byteBuffer) {
        int packetLength = getPackedLength();
        int pos = byteBuffer.position();
        for (int i = packetLength - 1; i >= 0; i--) {
            int twoDigits = length % 100;
            length /= 100;
            byteBuffer.put(pos + i, (byte) ((twoDigits / 10 << 4) + twoDigits % 10));
        }
        byteBuffer.position(pos + packetLength);
        return ;
    }

    @Override
    public int decodeLength(ByteBuffer byteBuffer) {
        int len = 0;
        for (int i = 0; i < (nDigits + 1) / 2; i++) {
            byte getByte = byteBuffer.get();
            len = 100 * len + ((getByte & 0xF0) >> 4) * 10 + (getByte & 0x0F);
        }
        return len;
    }


    public int getPackedLength() {
        return nDigits + 1 >> 1;
    }
}
