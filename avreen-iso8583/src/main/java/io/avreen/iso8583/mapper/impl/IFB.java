package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.mapper.api.ISOComponentMapper;

public class IFB {
    private IFB() {
    }
    public static ISOComponentMapper nop() {
        return new IF_EMPTY();
    }
    public static ISOComponentMapper NUM(int len, String description, boolean isLeftPadded) {
        return new ISOStringFieldMapper(len, description, LeftPadder.ZERO_PADDER,
                isLeftPadded ? BCDValueCodec.LEFT_PADDED : BCDValueCodec.RIGHT_PADDED,
                null);
    }

    public static ISOComponentMapper VARCHAR(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldMapper(len, description, null, AsciiValueCodec.INSTANCE, BCDValueLengthCodec.of(nDigits));
    }

    //
    public static ISOComponentMapper VARNUM(int len, String description, boolean isLeftPadded) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldMapper(len, description, null,
                isLeftPadded ? BCDValueCodec.LEFT_PADDED : BCDValueCodec.RIGHT_PADDED,
                BCDValueLengthCodec.of(nDigits));
    }
    //
    public static ISOComponentMapper VARNUM(int len, String description, boolean isLeftPadded, boolean fPadded) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldMapper(len, description, null,
                isLeftPadded ? BCDValueCodec.LEFT_PADDED :
                        fPadded ? BCDValueCodec.RIGHT_PADDED_F : BCDValueCodec.RIGHT_PADDED,
                BCDValueLengthCodec.of(nDigits));
    }
    //
    public static ISOComponentMapper BIN(int len, String description) {
        return new ISOBinaryFieldMapper(len, description, LiteralBinaryValueCodec.INSTANCE, null);
    }
    //
    public static ISOComponentMapper BITMAP(int len, String description) {
        return new IFB_BITMAP(len, description);
    }

    public static ISOComponentMapper VARBIN(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOBinaryFieldMapper(len, description, LiteralBinaryValueCodec.INSTANCE, BCDValueLengthCodec.of(nDigits));
    }


}
