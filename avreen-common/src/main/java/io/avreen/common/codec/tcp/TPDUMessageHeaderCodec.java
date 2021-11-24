package io.avreen.common.codec.tcp;

import io.avreen.common.context.MessageTypes;
import io.avreen.common.util.TPDUUtil;

/**
 * The class Tpdu message header codec.
 */
public class TPDUMessageHeaderCodec implements IMessageHeaderCodec {

    private byte[] destinationAddress;
    private byte[] sourceAddress;

    /**
     * Get destination address byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * Sets destination address.
     *
     * @param destinationAddress the destination address
     */
    public void setDestinationAddress(byte[] destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * Get source address byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Sets source address.
     *
     * @param sourceAddress the source address
     */
    public void setSourceAddress(byte[] sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    @Override
    public int getHeaderLength() {
        return 5;
    }

    @Override
    public byte[] encodeHeader(byte[] header, MessageTypes messageType, Integer rejectCode) {
        if (header == null) {
            /* build new header */
            return TPDUUtil.buildHeader(destinationAddress, sourceAddress);
        } else {
            if (MessageTypes.Request.equals(messageType))
                return TPDUUtil.buildHeader(destinationAddress, sourceAddress);
            else
                TPDUUtil.swapSourceAndDestination(header);
        }
        return header;
    }
}
