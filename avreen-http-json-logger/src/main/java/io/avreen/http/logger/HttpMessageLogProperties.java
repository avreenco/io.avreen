package io.avreen.http.logger;


/**
 * The class Http message log properties.
 */
public final class HttpMessageLogProperties {

    private String urlMatcherPattern;
    private boolean enable = true;
    private String[] includeFields;
    private String[] excludeFields;
    private String[] headerFields;
    private String[] messageTraceField = null;
    private String messageTraceFieldDelimiter = ",";

    /**
     * Is enable boolean.
     *
     * @return the boolean
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Sets enable.
     *
     * @param enable the enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Get include fields string [ ].
     *
     * @return the string [ ]
     */
    public String[] getIncludeFields() {
        return includeFields;
    }

    /**
     * Sets include fields.
     *
     * @param includeFields the include fields
     */
    public void setIncludeFields(String[] includeFields) {
        this.includeFields = includeFields;
    }

    /**
     * Get exclude fields string [ ].
     *
     * @return the string [ ]
     */
    public String[] getExcludeFields() {
        return excludeFields;
    }

    /**
     * Sets exclude fields.
     *
     * @param excludeFields the exclude fields
     */
    public void setExcludeFields(String[] excludeFields) {
        this.excludeFields = excludeFields;
    }

    /**
     * Get message trace field string [ ].
     *
     * @return the string [ ]
     */
    public String[] getMessageTraceField() {
        return messageTraceField;
    }

    /**
     * Sets message trace field.
     *
     * @param messageTraceField the message trace field
     */
    public void setMessageTraceField(String[] messageTraceField) {
        this.messageTraceField = messageTraceField;
    }

    /**
     * Gets message trace field delimiter.
     *
     * @return the message trace field delimiter
     */
    public String getMessageTraceFieldDelimiter() {
        return messageTraceFieldDelimiter;
    }

    /**
     * Sets message trace field delimiter.
     *
     * @param messageTraceFieldDelimiter the message trace field delimiter
     */
    public void setMessageTraceFieldDelimiter(String messageTraceFieldDelimiter) {
        this.messageTraceFieldDelimiter = messageTraceFieldDelimiter;
    }

    public String getUrlMatcherPattern() {
        return urlMatcherPattern;
    }

    public void setUrlMatcherPattern(String urlMatcherPattern) {
        this.urlMatcherPattern = urlMatcherPattern;
    }

    public String[] getHeaderFields() {
        return headerFields;
    }

    public HttpMessageLogProperties setHeaderFields(String[] headerFields) {
        this.headerFields = headerFields;
        return this;
    }
}
