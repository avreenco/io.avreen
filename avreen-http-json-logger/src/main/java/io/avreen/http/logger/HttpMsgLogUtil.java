package io.avreen.http.logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.avreen.common.context.IMsgContextAware;
import io.avreen.common.context.MessageTypes;
import io.avreen.common.log.LoggerDomain;
import io.avreen.http.HttpCodecUtil;
import io.avreen.http.common.HttpMsg;
import io.avreen.util.ChannelLogInfo;
import io.avreen.util.MsgLogUtil;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The class Http msg log util.
 */
public class HttpMsgLogUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private Set<String> excludeField = null;
    private Set<String> includeField = null;
    private Set<String> headerFields = null;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private String[] messageTraceField = null;
    private String messageTraceFieldDelimiter = ",";
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.logger.HttpMsgLogUtil");

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }


    /**
     * Instantiates a new Http msg log util.
     */
    public HttpMsgLogUtil() {

    }

    /**
     * Instantiates a new Http msg log util.
     *
     * @param excludeField the exclude field
     * @param includeField the include field
     */
    public HttpMsgLogUtil(Set<String> excludeField, Set<String> includeField) {
        this.excludeField = excludeField;
        this.includeField = includeField;
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

    public void setHeaderFields(Set<String> headerFields) {
        this.headerFields = headerFields;
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

    private static String getMT(HttpMsg httpMsg) {
        if (httpMsg.getMessageTypes() == MessageTypes.Reject)
            return "J";
        if (httpMsg.getMessageTypes() == MessageTypes.Response)
            return "S";
        return "R";
    }


    private Object serializeBody(Map<String, Object> logMap, HttpMsg httpMsg, HttpMsg.Direction direction, IValueToString valueToString) {
        if (httpMsg.getContent() == null)
            return null;
        boolean isJson = true;
        String contentTypeValue = httpMsg.getIgnoreCaseHeaderValue(HttpHeaderNames.CONTENT_TYPE.toString());
        if (contentTypeValue != null) {
            isJson = HttpUtil.getMimeType(contentTypeValue).toString().equals(HttpHeaderValues.APPLICATION_JSON.toString());
        }
        if (isJson) {
            if (includeField == null && excludeField == null && messageTraceField == null) {
                return httpMsg.getContent();
            }
            return serializeBodyOld(logMap, httpMsg, direction, valueToString);
        } else {
            NoneJsonContent noneJsonContent = new NoneJsonContent();
            if (excludeField != null && excludeField.contains("in-content") && direction == HttpMsg.Direction.incoming) {
                /* nothing */
            } else if (excludeField != null && excludeField.contains("out-content") && direction == HttpMsg.Direction.outgoing) {
                /* nothing */
            } else {

                if (direction == HttpMsg.Direction.outgoing)
                    noneJsonContent.setOutContent(httpMsg.getContent());
                else
                    noneJsonContent.setInContent(httpMsg.getContent());
            }
            return noneJsonContent;
        }
    }

    private Object serializeBodyOld(Map<String, Object> logMap, HttpMsg httpMsg, HttpMsg.Direction direction, IValueToString valueToString) {
        return serializeBodyOldOld(logMap, httpMsg, direction, valueToString);

    }

    private Object serializeBodyOldOld(Map<String, Object> logMap, HttpMsg httpMsg, HttpMsg.Direction direction, IValueToString valueToString) {
        Map map = null;
        try {
            map = objectMapper.readValue(httpMsg.getContent(), HashMap.class);
        } catch (JsonProcessingException e) {
            NoneJsonContent noneJsonContent = new NoneJsonContent();
            if (direction == HttpMsg.Direction.outgoing)
                noneJsonContent.setOutContent(httpMsg.getContent());
            else
                noneJsonContent.setInContent(httpMsg.getContent());
            return noneJsonContent;
        }
        Set fieldSet = map.keySet();
        if (includeField != null && includeField.size() > 0)
            fieldSet = includeField;

        HashMap<String, String> bodyMap = new HashMap<>();
        ArrayList<String> msgField = new ArrayList<>();
        for (Object fno : fieldSet) {
            String fieldNo = (String) fno;
            Object v = map.get(fieldNo);
            if (messageTraceField != null) {
                for (String s : messageTraceField) {
                    if (s.equalsIgnoreCase(fieldNo)) {
                        if (v != null)
                            msgField.add(v.toString());
                    }
                }
            }

            if (excludeField != null && excludeField.contains(fieldNo)) {
                v = "[*EXCLUDE*]";
            }
            if (!fieldSet.contains(fieldNo))
                continue;
            String val = null;
            if (v != null) {
                if (fieldNo.toUpperCase().contains("PASSWORD"))
                    val = "****";
                else {
                    if (valueToString == null)
                        val = v.toString();
                    else
                        val = valueToString.valueOf(fieldNo, v);
                }
            }
            if (val != null)
                bodyMap.put(fno.toString(), val);
        }
        if (msgField.size() > 0) {
            String key = String.join(messageTraceFieldDelimiter, msgField);
            MsgLogUtil.dumpELKFormat(logMap, "m-trace", key);

        }
        return bodyMap;
    }

    public static boolean existIgnoreCaseHeaderValue(Set<String> headers, String httpHeaderNames) {
        for (String string : headers) {
            if (string.equalsIgnoreCase(httpHeaderNames))
                return true;
        }
        return false;
    }

    private HashMap<String, String> serializeHeader(HttpMsg httpMsg) {
        HashMap<String, String> headerMap = new HashMap<>();
        Set<String> allHeaderFields = httpMsg.getHeaders().keySet();
        for (String fieldNo : allHeaderFields) {
            Object v = httpMsg.getIgnoreCaseHeaderValue(fieldNo);
            if (v == null)
                continue;
            if (headerFields != null && headerFields.size() > 0 && !existIgnoreCaseHeaderValue(headerFields, fieldNo))
                v = "[*EXCLUDE*]";
            headerMap.put(fieldNo, v.toString());
        }
        return headerMap;

    }

    private void dumpELKFormat(Map<String, Object> logMap, HttpMsg httpMsg, HttpMsg.Direction direction, IValueToString valueToString) {

        if (direction != null) {
            MsgLogUtil.dumpELKFormat(logMap, "dir", direction.equals(HttpMsg.Direction.incoming) ? "in" : "out");
        }
        if (httpMsg.isReject()) {
            MsgLogUtil.dumpELKFormat(logMap, "rjc", httpMsg.getRejectCode());
        }
        HashMap<String, String> headerString = serializeHeader(httpMsg);
        if (headerString != null)
            MsgLogUtil.dumpELKFormat(logMap, "header", headerString);


        if (!httpMsg.isReject()) {
            Object body = serializeBody(logMap, httpMsg, direction, valueToString);
            if (body != null)
                MsgLogUtil.dumpELKFormat(logMap, "body", body);
        }
    }

    /**
     * Build incoming http msg log string.
     *
     * @param ownerName      the owner name
     * @param httpMsg        the http message
     * @param channelLogInfo the channel log info
     * @param valueToString  the value to string
     * @return the string
     */
    public String buildIncomingHttpMsgLog(String ownerName, HttpMsg httpMsg, String uri, ChannelLogInfo channelLogInfo, IValueToString valueToString, Long requestTime, String internalTrace, String requestId, String message) throws JsonProcessingException {
        {
            HashMap<String, Object>  logMap = new HashMap<>();
            MsgLogUtil.dumpELKFormat(logMap, "dt", dateFormat.format(new Date()));
            MsgLogUtil.dumpELKFormat(logMap, "name", ownerName);
            if (uri != null)
                MsgLogUtil.dumpELKFormat(logMap, "uri", uri);
            else if (httpMsg.getUri() != null)
                MsgLogUtil.dumpELKFormat(logMap, "uri", httpMsg.getUri());
            MsgLogUtil.dumpELKFormat(logMap, "mt", getMT(httpMsg));
            if (httpMsg.getMethod() != null)
                MsgLogUtil.dumpELKFormat(logMap, "method", httpMsg.getMethod());
            if (httpMsg.getEndpointId() != null)
                MsgLogUtil.dumpELKFormat(logMap, "endpoint", httpMsg.getEndpointId());
            if (requestTime != null) {
                if (!httpMsg.isRequest() || httpMsg.isReject()) {
                    MsgLogUtil.dumpELKFormat(logMap, "rt", System.currentTimeMillis() - requestTime);
                }
            }
            if (!httpMsg.isRequest()) {
                if (!httpMsg.isReject()) {
                    Integer status = httpMsg.getStatus();
                    if (status == null)
                        status = 200;
                    MsgLogUtil.dumpELKFormat(logMap, "status", status);
                }
            }
            if (message != null)
                MsgLogUtil.dumpELKFormat(logMap, "msg", message);

            if (internalTrace != null)
                MsgLogUtil.dumpELKFormatKeyTrace(logMap, internalTrace);
            else
                MsgLogUtil.dumpELKFormatKeyTrace(logMap, httpMsg.getMsgContext());
            if (requestId != null)
                MsgLogUtil.dumpELKFormat(logMap, "r-id", requestId);

            String realIp = HttpCodecUtil.getRealIp(httpMsg);
            if (realIp != null && httpMsg.isRequest())
                MsgLogUtil.dumpELKFormat(logMap, "real-ip", realIp);

            MsgLogUtil.dumpELKFormatChannel(logMap, channelLogInfo);
            MsgLogUtil.dumpELKFormatNode(logMap, httpMsg.getMsgContext());

            dumpELKFormat(logMap, httpMsg, HttpMsg.Direction.incoming, valueToString);
            return objectMapper.writeValueAsString(logMap);
        }
    }

    /**
     * Build outgoing http msg log string.
     *
     * @param ownerName      the owner name
     * @param httpMsg        the http message
     * @param channelLogInfo the channel log info
     * @param valueToString  the value to string
     * @return the string
     */
    public String buildOutgoingHttpMsgLog(String ownerName, HttpMsg httpMsg, String uri, ChannelLogInfo channelLogInfo, IValueToString valueToString, Long requestTime, String internalTrace, String requestId, String message) throws JsonProcessingException {
        {
            HashMap<String, Object> logMap = new HashMap<>();
            MsgLogUtil.dumpELKFormat(logMap, "dt", dateFormat.format(new Date()));
            MsgLogUtil.dumpELKFormat(logMap, "name", ownerName);
            if (uri != null)
                MsgLogUtil.dumpELKFormat(logMap, "uri", uri);
            else if (httpMsg.getUri() != null)
                MsgLogUtil.dumpELKFormat(logMap, "uri", httpMsg.getUri());
            MsgLogUtil.dumpELKFormat(logMap, "mt", getMT(httpMsg));
            if (!httpMsg.isRequest()) {
                if (!httpMsg.isReject()) {
                    Integer status = httpMsg.getStatus();
                    if (status == null)
                        status = 200;
                    MsgLogUtil.dumpELKFormat(logMap, "status", status);
                }
            }
            if (message != null)
                MsgLogUtil.dumpELKFormat(logMap, "msg", message);

            if (httpMsg.getEndpointId() != null)
                MsgLogUtil.dumpELKFormat(logMap, "endpoint", httpMsg.getEndpointId());
            if (requestTime != null) {
                if (!httpMsg.isRequest() || httpMsg.isReject()) {
                    MsgLogUtil.dumpELKFormat(logMap, "rt", System.currentTimeMillis() - requestTime);
                }
            }
            if (internalTrace != null)
                MsgLogUtil.dumpELKFormatKeyTrace(logMap, internalTrace);
            else
                MsgLogUtil.dumpELKFormatKeyTrace(logMap, httpMsg.getMsgContext());
            if (requestId != null)
                MsgLogUtil.dumpELKFormat(logMap, "r-id", requestId);

            MsgLogUtil.dumpELKFormatChannel(logMap, channelLogInfo);

            MsgLogUtil.dumpELKFormatNode(logMap, httpMsg instanceof IMsgContextAware ? ((IMsgContextAware) httpMsg).getMsgContext() : null);
            dumpELKFormat(logMap, httpMsg, HttpMsg.Direction.outgoing, valueToString);
            return objectMapper.writeValueAsString(logMap);
        }
    }
}
