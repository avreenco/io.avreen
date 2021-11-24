package io.avreen.iso8583.packager.impl;


/**
 * The class Ifa llbinary.
 */
public class IFA_LLBINARY extends ISOBinaryFieldPackager {
    /**
     * Instantiates a new Ifa llbinary.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LLBINARY(int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, AsciiPrefixer.LL);
        checkLength(len, 99);
    }

    public void setLength(int len) {
        checkLength(len, 99);
        super.setLength(len);
    }
}

