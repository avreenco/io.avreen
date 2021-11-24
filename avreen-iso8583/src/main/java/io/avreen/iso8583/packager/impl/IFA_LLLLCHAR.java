package io.avreen.iso8583.packager.impl;

/**
 * The class Ifa llllchar.
 */
public class IFA_LLLLCHAR extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifa llllchar.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LLLLCHAR(int len, String description) {
        super(len, description, null, AsciiInterpreter.INSTANCE, AsciiPrefixer.LLLL);
        checkLength(len, 999);
    }

    public void setLength(int len) {
        checkLength(len, 999);
        super.setLength(len);
    }
}
