package io.avreen.iso8583.packager.impl;


/**
 * The class Ifa lbinary.
 */
public class IFA_LBINARY extends ISOBinaryFieldPackager {
    /**
     * Instantiates a new Ifa lbinary.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LBINARY(int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, AsciiPrefixer.L);
        checkLength(len, 9);
    }

    public void setLength(int len) {
        checkLength(len, 9);
        super.setLength(len);
    }
}

