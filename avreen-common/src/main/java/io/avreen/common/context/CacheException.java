package io.avreen.common.context;

/**
 * The class Cache exception.
 */
public class CacheException extends RuntimeException {
    /**
     * The constant DUPLICATE_KEY_EXCEPTION.
     */
    public static final int DUPLICATE_KEY_EXCEPTION = 1;
    private int errorCode;

    /**
     * Instantiates a new Cache exception.
     *
     * @param errorCode the error code
     * @param message   the message
     */
    public CacheException(int errorCode, String message) {
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
