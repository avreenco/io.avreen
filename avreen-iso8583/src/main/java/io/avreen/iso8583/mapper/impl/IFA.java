package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.mapper.api.ISOComponentMapper;

public class IFA {
    private IFA() {
    }

    public static ISOComponentMapper CHAR(int len, String description) {
        return new ISOStringFieldMapper(len, description, RightTPadder.SPACE_PADDER, AsciiValueCodec.INSTANCE, null);
    }

    public static ISOComponentMapper nop() {
        return new IF_EMPTY();
    }

    public static ISOComponentMapper NUM(int len, String description) {
        return new ISOStringFieldMapper(len, description, LeftPadder.ZERO_PADDER, AsciiValueCodec.INSTANCE, null);
    }

    public static ISOComponentMapper VARCHAR(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldMapper(len, description, null, AsciiValueCodec.INSTANCE, AsciiValueLengthCodec.of(nDigits));
    }

    public static ISOComponentMapper VARNUM(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOStringFieldMapper(len, description, null, AsciiValueCodec.INSTANCE, AsciiValueLengthCodec.of(nDigits));
    }

    public static ISOComponentMapper BIN(int len, String description) {
        return new ISOBinaryFieldMapper(len, description, AsciiHexValueCodec.INSTANCE, null);
    }

    public static ISOComponentMapper BITMAP(int len, String description) {
        return new IFA_BITMAP(len, description);
    }

    public static ISOComponentMapper VARBIN(int len, String description) {
        int nDigits = String.valueOf(len).length();
        return new ISOBinaryFieldMapper(len, description, LiteralBinaryValueCodec.INSTANCE, AsciiValueLengthCodec.of(nDigits));
    }


}
