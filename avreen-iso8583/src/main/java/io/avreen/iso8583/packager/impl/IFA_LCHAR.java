package io.avreen.iso8583.packager.impl;

/**
 * The class Ifa lchar.
 */
public class IFA_LCHAR extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifa lchar.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_LCHAR(int len, String description) {
        super(len, description, null, AsciiInterpreter.INSTANCE, AsciiPrefixer.L);
        checkLength(len, 9);
    }

    public void setLength(int len) {
        checkLength(len, 9);
        super.setLength(len);
    }
}
