package io.avreen.iso8583.packager.impl;

/**
 * The class Ifa lllchar.
 */
public class IFA_LLLCHAR extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifa lllchar.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LLLCHAR(int len, String description) {
        super(len, description, null, AsciiInterpreter.INSTANCE, AsciiPrefixer.LLL);
        checkLength(len, 999);
    }

    public void setLength(int len) {
        checkLength(len, 999);
        super.setLength(len);
    }
}
