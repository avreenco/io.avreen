package io.avreen.iso8583.packager.impl;

/**
 * The class Ifa llchar.
 */
public class IFA_LLCHAR extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifa llchar.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LLCHAR(int len, String description) {
        super(len, description, null, AsciiInterpreter.INSTANCE, AsciiPrefixer.LL);
        checkLength(len, 99);
    }

    public void setLength(int len) {
        checkLength(len, 99);
        super.setLength(len);
    }
}
