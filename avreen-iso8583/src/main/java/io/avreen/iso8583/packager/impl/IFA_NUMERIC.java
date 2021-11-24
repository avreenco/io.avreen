package io.avreen.iso8583.packager.impl;

/**
 * The class Ifa numeric.
 */
public class IFA_NUMERIC extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifa numeric.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_NUMERIC(int len, String description) {
        super(len, description, LeftPadder.ZERO_PADDER, AsciiInterpreter.INSTANCE, null);
    }
}
