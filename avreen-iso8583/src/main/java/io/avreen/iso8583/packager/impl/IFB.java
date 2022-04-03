package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.api.ISOComponentPackager;

public class IFB {
    private IFB() {
    }
    public static ISOComponentPackager nop() {
        return new EMPTYFIELD();
    }
    public static ISOComponentPackager NUM(int len, String description, boolean isLeftPadded) {
        return new ISOStringFieldPackager(len, description, LeftPadder.ZERO_PADDER,
                isLeftPadded ? BCDValueCodec.LEFT_PADDED : BCDValueCodec.RIGHT_PADDED,
                null);
    }

    public static ISOComponentPackager VARCHAR(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldPackager(len, description, null, AsciiValueCodec.INSTANCE, BCDValueLengthCodec.of(nDigits));
    }

    //
    public static ISOComponentPackager VARNUM(int len, String description, boolean isLeftPadded) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldPackager(len, description, null,
                isLeftPadded ? BCDValueCodec.LEFT_PADDED : BCDValueCodec.RIGHT_PADDED,
                BCDValueLengthCodec.of(nDigits));
    }
    //
    public static ISOComponentPackager VARNUM(int len, String description, boolean isLeftPadded, boolean fPadded) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldPackager(len, description, null,
                isLeftPadded ? BCDValueCodec.LEFT_PADDED :
                        fPadded ? BCDValueCodec.RIGHT_PADDED_F : BCDValueCodec.RIGHT_PADDED,
                BCDValueLengthCodec.of(nDigits));
    }
    //
    public static ISOComponentPackager BIN(int len, String description) {
        return new ISOBinaryFieldPackager(len, description, LiteralBinaryValueCodec.INSTANCE, null);
    }
    //
    public static ISOComponentPackager BITMAP(int len, String description) {
        return new IFB_BITMAP(len, description);
    }

    public static ISOComponentPackager VARBIN(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOBinaryFieldPackager(len, description, LiteralBinaryValueCodec.INSTANCE, BCDValueLengthCodec.of(nDigits));
    }


}
