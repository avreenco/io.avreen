package io.avreen.iso8583.packager.impl;

/**
 * The class Ifa llllbinary.
 */
public class IFA_LLLLBINARY extends ISOBinaryFieldPackager {
    /**
     * Instantiates a new Ifa llllbinary.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LLLLBINARY(int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, AsciiPrefixer.LLLL);
        checkLength(len, 999);
    }

    public void setLength(int len) {
        checkLength(len, 999);
        super.setLength(len);
    }
}
