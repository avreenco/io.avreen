package io.avreen.common.util;


/**
 * The class Tpdu util.
 */
public class TPDUUtil {
    /**
     * Build header byte [ ].
     *
     * @param destinationAddress the destination address
     * @param sourceAddress      the source address
     * @return the byte [ ]
     */
    public static byte[] buildHeader(byte[] destinationAddress, byte[] sourceAddress) {
        byte[] header = new byte[5];
        header[0] = 0x60;
        System.arraycopy(destinationAddress, 0, header, 1, 2);
        System.arraycopy(sourceAddress, 0, header, 3, 2);
        return header;
    }

    /**
     * Swap source and destination.
     *
     * @param tpdu the tpdu
     */
    public static void swapSourceAndDestination(byte[] tpdu) {
        byte[] source = new byte[2];
        System.arraycopy(tpdu, 3, source, 0, 2);
        System.arraycopy(tpdu, 1, tpdu, 3, 2);
        System.arraycopy(source, 0, tpdu, 1, 2);
    }

    /**
     * Get destination address byte [ ].
     *
     * @param header the header
     * @return the byte [ ]
     */
    public static byte[] getDestinationAddress(byte[] header) {
        byte[] origin = new byte[2];
        System.arraycopy(header, 1, origin, 0, 2);
        return origin;

    }

    /**
     * Get source address byte [ ].
     *
     * @param header the header
     * @return the byte [ ]
     */
    public static byte[] getSourceAddress(byte[] header) {
        byte[] destiny = new byte[2];
        System.arraycopy(header, 3, destiny, 0, 2);
        return destiny;

    }

    /**
     * Sets source address.
     *
     * @param header        the header
     * @param sourceAddress the source address
     */
    public static void setSourceAddress(byte[] header, byte[] sourceAddress) {
        System.arraycopy(header, 3, sourceAddress, 0, 2);
    }

    /**
     * Sets destination address.
     *
     * @param header             the header
     * @param destinationAddress the destination address
     */
    public static void setDestinationAddress(byte[] header, byte[] destinationAddress) {
        System.arraycopy(header, 1, destinationAddress, 0, 2);

    }

}
