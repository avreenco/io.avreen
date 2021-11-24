package io.avreen.common.codec.tcp;

/**
 * The interface Reject code support codec.
 */
public interface RejectCodeSupportCodec {
    /**
     * Is reject boolean.
     *
     * @param header the header
     * @return the boolean
     */
    boolean isReject(byte[] header);

    /**
     * Gets reject code.
     *
     * @param header the header
     * @return the reject code
     */
    int getRejectCode(byte[] header);

}
