package io.avreen.iso8583.packager.impl;

/**
 * The class Ifa lllbinary.
 */
public class IFA_LLLBINARY extends ISOBinaryFieldPackager {
    /**
     * Instantiates a new Ifa lllbinary.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LLLBINARY(int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, AsciiPrefixer.LLL);
        checkLength(len, 999);
    }

    public void setLength(int len) {
        checkLength(len, 999);
        super.setLength(len);
    }
}
