package io.avreen.iso8583.packager.impl;


/**
 * The class Ifa binary.
 */
public class IFA_BINARY extends ISOBinaryFieldPackager {
    /**
     * Instantiates a new Ifa binary.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_BINARY(int len, String description) {
        super(len, description, AsciiHexInterpreter.INSTANCE, null);
    }
}
