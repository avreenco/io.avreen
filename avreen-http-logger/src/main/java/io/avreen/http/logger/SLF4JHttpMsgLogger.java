package io.avreen.http.logger;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.avreen.common.context.ContextKeyUtil;
import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.MessageDiagnosticKey;
import io.avreen.http.common.HttpMsg;
import io.avreen.util.ChannelLogInfo;
import io.avreen.util.MsgLogUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * The class Slf 4 j http msg logger.
 */
public class SLF4JHttpMsgLogger {
    private static final InternalLogger logger_in = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.channel.SLF4JHttpMsgLogHandler.in");
    private static final InternalLogger logger_out = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.channel.SLF4JHttpMsgLogHandler.out");
    private static final InternalLogger logger_in_reject = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.channel.SLF4JHttpMsgLogHandler.in.reject");
    private static final InternalLogger logger_out_reject = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.channel.SLF4JHttpMsgLogHandler.out.reject");

    private String name;
    private String uri;
    private Set<String> excludeField = new HashSet<>();
    private Set<String> includeField = new HashSet<>();
    private Set<String> headerFields = new HashSet<>();
    private IValueToString valueToString;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String[] messageTraceField = null;
    private String messageTraceFieldDelimiter = ",";
    private long requestTime;
    private String requestTrace = null;
    private String  requestId;
    private String   message;


    /**
     * Instantiates a new Slf 4 j http msg logger.
     */
    public SLF4JHttpMsgLogger() {
        this(null);

    }

    public SLF4JHttpMsgLogger(String uri) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.uri = uri;
        requestTime = System.currentTimeMillis();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public SLF4JHttpMsgLogger setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets exclude field.
     *
     * @return the exclude field
     */
    public Set<String> getExcludeField() {
        return excludeField;
    }

    /**
     * Sets exclude field.
     *
     * @param excludeField the exclude field
     */
    public void setExcludeField(Set<String> excludeField) {
        this.excludeField = excludeField;
    }

    /**
     * Gets include field.
     *
     * @return the include field
     */
    public Set<String> getIncludeField() {
        return includeField;
    }

    /**
     * Sets include field.
     *
     * @param includeField the include field
     */
    public void setIncludeField(Set<String> includeField) {
        this.includeField = includeField;
    }

    public Set<String> getHeaderFields() {
        return headerFields;
    }

    public SLF4JHttpMsgLogger setHeaderFields(Set<String> headerFields) {
        this.headerFields = headerFields;
        return this;
    }

    /**
     * Gets value to string.
     *
     * @return the value to string
     */
    public IValueToString getValueToString() {
        return valueToString;
    }

    /**
     * Sets value to string.
     *
     * @param valueToString the value to string
     */
    public void setValueToString(IValueToString valueToString) {
        this.valueToString = valueToString;
    }

    /**
     * Is incoming log enabled boolean.
     *
     * @return the boolean
     */
    public boolean isIncomingLogEnabled() {
        return logger_in.isInfoEnabled();
    }

    /**
     * Is outgoing log enabled boolean.
     *
     * @return the boolean
     */
    public boolean isOutgoingLogEnabled() {
        return logger_out.isInfoEnabled();
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

    private HttpMsg writeHttpMsg(Object logObject, boolean isRequest) {
        HttpMsg httpMsg = new HttpMsg();
        httpMsg.setRequest(isRequest);

        String body;
        try {
            body = objectMapper.writeValueAsString(logObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        httpMsg.setContent(body);
        return httpMsg;
    }


    /**
     * Log outgoing object.
     *
     * @param logObject      the log object
     * @param isRequest      the is request
     * @param channelLogInfo the channel log info
     */
    public void logOutgoingObject(Object logObject, boolean isRequest, ChannelLogInfo channelLogInfo, MsgContext msgContext) throws JsonProcessingException {
        if (!this.isOutgoingLogEnabled())
            return;
        HttpMsg httpMsg = writeHttpMsg(logObject, isRequest);
        if (msgContext != null)
            httpMsg.setMsgContext(msgContext);
        this.logOutgoing(httpMsg, channelLogInfo);

    }

    /**
     * Log incoming object.
     *
     * @param logObject      the log object
     * @param isRequest      the is request
     * @param channelLogInfo the channel log info
     */
    public void logIncomingObject(Object logObject, boolean isRequest, String serviceName, ChannelLogInfo channelLogInfo, MsgContext msgContext) throws JsonProcessingException {
        if (!this.isIncomingLogEnabled())
            return;
        HttpMsg httpMsg = writeHttpMsg(logObject, isRequest);
        httpMsg.setEndpointId(serviceName);
        if (msgContext != null)
            httpMsg.setMsgContext(msgContext);
        this.logIncoming(httpMsg, channelLogInfo);

    }

    public String getRequestId() {
        return requestId;
    }

    /**
     * Log incoming.
     *
     * @param httpMsg        the dynamic http message
     * @param channelLogInfo the channel log info
     */
    public void logIncoming(HttpMsg httpMsg, ChannelLogInfo channelLogInfo) throws JsonProcessingException {
        if (logger_in.isInfoEnabled()) {
            HttpMsgLogUtil httpMsgLogUtil = new HttpMsgLogUtil(excludeField, includeField);
            httpMsgLogUtil.setMessageTraceField(messageTraceField);
            httpMsgLogUtil.setMessageTraceFieldDelimiter(messageTraceFieldDelimiter);
            httpMsgLogUtil.setHeaderFields(headerFields);
            if (getName() != null)
                MessageDiagnosticKey.put("logger.name", getName());
            if (httpMsg.isRequest())
            {
                requestTrace = MsgLogUtil.getInternalTrace(httpMsg.getMsgContext());
                requestId= ContextKeyUtil.genRandomString();
            }

            String logEvent = httpMsgLogUtil.buildIncomingHttpMsgLog(getName(), httpMsg, this.uri, channelLogInfo, valueToString, requestTime,requestTrace,requestId,message);
            logEvent = normalizeString(logEvent);
            if (httpMsg.isReject())
                logger_in_reject.info(logEvent);
            else
                logger_in.info(logEvent);

        }
    }

    /**
     * Log outgoing.
     *
     * @param httpMsg the dynamic http message
     * @param channelLogInfo     the channel log info
     */
    public void logOutgoing(HttpMsg httpMsg, ChannelLogInfo channelLogInfo) throws JsonProcessingException {
        if (logger_out.isInfoEnabled()) {
            if (getName() != null)
                MessageDiagnosticKey.put("logger.name", getName());
            HttpMsgLogUtil httpMsgLogUtil = new HttpMsgLogUtil(excludeField, includeField);
            httpMsgLogUtil.setHeaderFields(headerFields);

            httpMsgLogUtil.setMessageTraceField(messageTraceField);
            httpMsgLogUtil.setMessageTraceFieldDelimiter(messageTraceFieldDelimiter);
            if (getName() != null)
                MessageDiagnosticKey.put("logger.name", getName());
            if (httpMsg.isRequest() || httpMsg.isReject())
            {
                requestTrace = MsgLogUtil.getInternalTrace(httpMsg.getMsgContext());
                requestId= ContextKeyUtil.genRandomString();
            }


            String logEvent = httpMsgLogUtil.buildOutgoingHttpMsgLog(getName(), httpMsg, this.uri, channelLogInfo, valueToString, requestTime,requestTrace,requestId,message);
            logEvent = normalizeString(logEvent);
            if (httpMsg.isReject())
                logger_out_reject.info(logEvent);
            else
                logger_out.info(logEvent);
        }

    }

    private String normalizeString(String raw) {
        if (raw == null)
            return "";
        return raw.replaceAll("\n", "").replace("\r", "");
    }
}