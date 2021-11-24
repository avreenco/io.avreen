package io.avreen.iso8583.util;

/**
 * The class Invalid field exception.
 */
public class InvalidFieldException extends Exception {
    private static final long serialVersionUID = 1L;
    /**
     * The Field number.
     */
    int fieldNumber = 0;
    /**
     * The Error code.
     */
    String errorCode = IErrorCode.InavlidFieldFormat;

    /**
     * Instantiates a new Invalid field exception.
     *
     * @param fieldNumber the field number
     * @param errorCode   the error code
     * @param message     the message
     */
    public InvalidFieldException(int fieldNumber, String errorCode, String message) {
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
    public InvalidFieldException(int fieldNumber, String errorCode, Throwable e) {
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
    public InvalidFieldException(int fieldNumber, Throwable e) {
        super(e);
        this.fieldNumber = fieldNumber;
        this.errorCode = IErrorCode.InvalifFormat;
    }

    /**
     * Gets error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
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
