package io.avreen.iso8583.packager.impl;

/**
 * The class Ifb binary.
 */
public class IFB_BINARY extends ISOBinaryFieldPackager {
    /**
     * Instantiates a new Ifb binary.
     *
     * @param len         the len
     * @param description the description
     */
    public IFB_BINARY(int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, null);
    }
}
