
package io.avreen.iso8583.packager.impl;


import io.avreen.iso8583.packager.impl.base.Padder;

/**
 * The class Left padder.
 */
public class LeftPadder implements Padder<String> {
    /**
     * The constant ZERO_PADDER.
     */
    public static final LeftPadder ZERO_PADDER = new LeftPadder('0');

    private char pad;

    /**
     * Instantiates a new Left padder.
     *
     * @param pad the pad
     */
    public LeftPadder(char pad) {
        this.pad = pad;
    }

    public String pad(String data, int maxLength) {
        StringBuilder padded = new StringBuilder(maxLength);
        int len = data.length();
        if (len > maxLength) {
            throw new RuntimeException("Data is too long. Max = " + maxLength);
        } else {
            for (int i = maxLength - len; i > 0; i--) {
                padded.append(pad);
            }
            padded.append(data);
        }
        return padded.toString();
    }

}
