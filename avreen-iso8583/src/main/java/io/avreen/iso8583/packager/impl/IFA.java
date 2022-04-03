package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.api.ISOComponentPackager;

public class IFA {
    private IFA() {
    }

    public static ISOComponentPackager CHAR(int len, String description) {
        return new ISOStringFieldPackager(len, description, RightTPadder.SPACE_PADDER, AsciiValueCodec.INSTANCE, null);
    }

    public static ISOComponentPackager nop() {
        return new EMPTYFIELD();
    }

    public static ISOComponentPackager NUM(int len, String description) {
        return new ISOStringFieldPackager(len, description, LeftPadder.ZERO_PADDER, AsciiValueCodec.INSTANCE, null);
    }

    public static ISOComponentPackager VARCHAR(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldPackager(len, description, null, AsciiValueCodec.INSTANCE, AsciiValueLengthCodec.of(nDigits));
    }

    public static ISOComponentPackager VARNUM(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldPackager(len, description, null, AsciiValueCodec.INSTANCE, AsciiValueLengthCodec.of(nDigits));
    }

    public static ISOComponentPackager BIN(int len, String description) {
        return new ISOBinaryFieldPackager(len, description, AsciiHexValueCodec.INSTANCE, null);
    }

    public static ISOComponentPackager BITMAP(int len, String description) {
        return new IFA_BITMAP(len, description);
    }

    public static ISOComponentPackager VARBIN(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOBinaryFieldPackager(len, description, LiteralBinaryValueCodec.INSTANCE, AsciiValueLengthCodec.of(nDigits));
    }


}
