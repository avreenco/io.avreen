package io.avreen.common.mux;

/**
 * The class Multiplexer exception.
 */
public class MultiplexerException extends RuntimeException {
    /**
     * The constant DUPLICATE_KEY_EXCEPTION.
     */
    public static final int DUPLICATE_KEY_EXCEPTION = 1;
    /**
     * The constant QUEUE_FULL.
     */
    public static final int QUEUE_FULL = 2;
    /**
     * The constant DISCONNECTED.
     */
    public static final int DISCONNECTED = 3;

    private int errorCode;

    /**
     * Instantiates a new Multiplexer exception.
     *
     * @param errorCode the error code
     * @param message   the message
     */
    public MultiplexerException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Gets error code.
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }
}
