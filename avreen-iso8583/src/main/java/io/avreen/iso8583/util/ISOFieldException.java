package io.avreen.iso8583.util;

/**
 * The class Invalid field exception.
 */
public class ISOFieldException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * The Field number.
     */
    int fieldNumber = 0;
    /**
     * The Error code.
     */
    int errorCode ;

    /**
     * Instantiates a new Invalid field exception.
     *
     * @param fieldNumber the field number
     * @param errorCode   the error code
     * @param message     the message
     */
    public ISOFieldException(int fieldNumber, int errorCode, String message) {
        super(message);
        this.fieldNumber = fieldNumber;
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Invalid field exception.
     *
     * @param fieldNumber the field number
     * @param errorCode   the error code
     * @param e           the e
     */
    public ISOFieldException(int fieldNumber, int errorCode, Throwable e) {
        super(e);
        this.fieldNumber = fieldNumber;
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Invalid field exception.
     *
     * @param fieldNumber the field number
     * @param e           the e
     */
    public ISOFieldException(int fieldNumber, Throwable e) {
        super(e);
        this.fieldNumber = fieldNumber;
        this.errorCode = IErrorCode.InvalidFieldFormat;
    }

    /**
     * Gets error code.
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Gets field number.
     *
     * @return the field number
     */
    public Integer getFieldNumber() {
        return fieldNumber;
    }

    @Override
    public String getMessage() {
        return "exception in field={" + fieldNumber + "}" + " and error code={" + this.errorCode + "} and message=" + super.getMessage();
    }

}
