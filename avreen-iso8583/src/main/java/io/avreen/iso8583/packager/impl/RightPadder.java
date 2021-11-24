
package io.avreen.iso8583.packager.impl;


import io.avreen.iso8583.packager.impl.base.Padder;

/**
 * The class Right padder.
 */
public class RightPadder implements Padder<String> {
    /**
     * The constant SPACE_PADDER.
     */
    public static final RightPadder SPACE_PADDER = new RightPadder(' ');

    private char pad;

    /**
     * Instantiates a new Right padder.
     *
     * @param pad the pad
     */
    public RightPadder(char pad) {
        this.pad = pad;
    }

    public String pad(String data, int maxLength) {
        int len = data.length();

        if (len < maxLength) {
            StringBuilder padded = new StringBuilder(maxLength);
            padded.append(data);
            for (; len < maxLength; len++) {
                padded.append(pad);
            }
            data = padded.toString();
        } else if (len > maxLength) {
            throw new RuntimeException("Data is too long. Max = " + maxLength);
        }
        return data;
    }

}
