package io.avreen.iso8583.packager.impl;

/**
 * The class Ifa llnum.
 */
public class IFA_LLNUM extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifa llnum.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LLNUM(int len, String description) {
        super(len, description, null, AsciiInterpreter.INSTANCE, AsciiPrefixer.LL);
        checkLength(len, 99);
    }

    public void setLength(int len) {
        checkLength(len, 99);
        super.setLength(len);
    }
}
