package io.avreen.iso8583.packager.impl;

/**
 * The class If char.
 */
public class IF_CHAR extends ISOStringFieldPackager {
    /**
     * Instantiates a new If char.
     *
     * @param len         the len
     * @param description the description
     */
    public IF_CHAR(int len, String description) {
        super(len, description, RightTPadder.SPACE_PADDER, AsciiInterpreter.INSTANCE, null);
    }
}
