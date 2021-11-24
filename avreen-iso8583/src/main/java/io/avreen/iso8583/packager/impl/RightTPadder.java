package io.avreen.iso8583.packager.impl;

/**
 * The class Right t padder.
 */
public class RightTPadder extends RightPadder {
    /**
     * The constant SPACE_PADDER.
     */
    public static final RightTPadder SPACE_PADDER = new RightTPadder(' ');

    /**
     * Instantiates a new Right t padder.
     *
     * @param pad the pad
     */
    public RightTPadder(char pad) {
        super(pad);
    }

    public String pad(String data, int maxLength) {
        if (data.length() > maxLength) {
            return super.pad(data.substring(0, maxLength), maxLength);
        } else {
            return super.pad(data, maxLength);
        }
    }
}