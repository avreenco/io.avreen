package io.avreen.iso8583.packager.impl;

/**
 * The class Ifb lllhchar.
 */
public class IFB_LLLHCHAR extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifb lllhchar.
     *
     * @param len         the len
     * @param description the description
     */
    public IFB_LLLHCHAR(int len, String description) {
        super(len, description, null, AsciiInterpreter.INSTANCE, BinaryPrefixer.BB);
        checkLength(len, 65535);
    }

    public void setLength(int len) {
        checkLength(len, 65535);
        super.setLength(len);
    }
}

