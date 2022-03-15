package io.avreen.iso8583.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.avreen.iso8583.common.ISOComponent;
import io.avreen.iso8583.common.ISOComponentDumper;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.util.ChannelLogInfo;
import io.avreen.util.MsgLogUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The class Iso msg log util.
 */
public class ISOMsgLogUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private Set<Integer> excludeField = new HashSet<>();
    private Set<Integer> includeField = new HashSet<>();
    private Integer[] messageTraceField = null;
    private String messageTraceFieldDelimiter = ",";
    private boolean logHeader = true;
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Instantiates a new Iso msg log util.
     */
    public ISOMsgLogUtil() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Instantiates a new Iso msg log util.
     *
     * @param excludeField the exclude field
     * @param includeField the include field
     */
    public ISOMsgLogUtil(Set<Integer> excludeField, Set<Integer> includeField) {
        this();
        this.excludeField = excludeField;
        this.includeField = includeField;
    }

    /**
     * Get message trace field integer [ ].
     *
     * @return the integer [ ]
     */
    public Integer[] getMessageTraceField() {
        return messageTraceField;
    }

    /**
     * Sets message trace field.
     *
     * @param messageTraceField the message trace field
     */
    public void setMessageTraceField(Integer[] messageTraceField) {
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

    /**
     * Is log header boolean.
     *
     * @return the boolean
     */
    public boolean isLogHeader() {
        return logHeader;
    }

    /**
     * Sets log header.
     *
     * @param logHeader the log header
     */
    public void setLogHeader(boolean logHeader) {
        this.logHeader = logHeader;
    }

    private void dumpELKFormat(Map<String,Object> logMap, ISOMsg isoMsg, ISOMsg.Direction direction) {

        if (direction != null) {
            MsgLogUtil.dumpELKFormat(logMap, "dir", direction.equals(ISOMsg.Direction.incoming) ? "in" : "out");
        }
        if (isoMsg.isReject()) {
            MsgLogUtil.dumpELKFormat(logMap, "rjc", isoMsg.getRejectCode());
        }


        if (isoMsg.getHeader() != null) {
            if (logHeader)
                MsgLogUtil.dumpELKFormat(logMap, "header", ISOUtil.hexString(isoMsg.getHeader()));
        }


        if (!isoMsg.isReject()) {
            Set fieldSet = isoMsg.fieldSet();
            if (includeField != null && includeField.size() > 0)
                fieldSet = includeField;

            HashMap<String,String>  bodyMap = new HashMap<>();
            for (Object fno : fieldSet) {
                int fieldNo = (int) fno;
                if (excludeField != null && excludeField.contains(fieldNo))
                    continue;
                if (!isoMsg.contains(fieldNo))
                    continue;
                ISOComponent isoComponent = isoMsg.getIsoComponent(fieldNo);
                if (isoComponent.getValue() == null)
                    continue;
                String  key = "i"+fno;
                ISOComponentDumper isoComponentDumper = DumpUtil.getIsoComponentDumper(isoComponent);
                Object valueString = isoComponentDumper.convertToString(isoComponent);
                if(valueString!=null)
                    bodyMap.put(key , valueString.toString());
            }
            MsgLogUtil.dumpELKFormat(logMap, "body", bodyMap);
        }
    }

    private void dumpELKFormatMessageKey(Map<String,Object> logMap, ISOMsg isoMsg) {

        if (messageTraceField == null)
            return;
        ArrayList<String> msgField = new ArrayList<>();
        for (Integer f : messageTraceField) {
            if (isoMsg.contains(f)) {
                if (isoMsg.getString(f) != null)
                    msgField.add(isoMsg.getString(f));
            }
        }
        if (msgField.size() == 0)
            return;
        String key = String.join(messageTraceFieldDelimiter, msgField);
        MsgLogUtil.dumpELKFormat(logMap, "m-trace", key);
    }

    /**
     * Build incoming iso msg log string.
     *
     * @param ownerName      the owner name
     * @param isoMsg         the iso msg
     * @param channelLogInfo the channel log info
     * @return the string
     */
    public String buildIncomingISOMsgLog(String ownerName, ISOMsg isoMsg, ChannelLogInfo channelLogInfo) throws JsonProcessingException {
        {
            Map<String,Object> logMap = new HashMap<>();
            MsgLogUtil.dumpELKFormat(logMap, "dt", dateFormat.format(new Date()));
            MsgLogUtil.dumpELKFormat(logMap, "name", ownerName);
            MsgLogUtil.dumpELKFormatKeyTrace(logMap, isoMsg.getMsgContext());
            dumpELKFormatMessageKey(logMap, isoMsg);
            MsgLogUtil.dumpELKFormatChannel(logMap, channelLogInfo);
            MsgLogUtil.dumpELKFormatNode(logMap, isoMsg.getMsgContext());
            dumpELKFormat(logMap, isoMsg, ISOMsg.Direction.incoming);
            return objectMapper.writeValueAsString(logMap);
        }
    }

    /**
     * Build outgoing iso msg log string.
     *
     * @param ownerName      the owner name
     * @param isoMsg         the iso msg
     * @param channelLogInfo the channel log info
     * @return the string
     */
    public String buildOutgoingISOMsgLog(String ownerName, ISOMsg isoMsg, ChannelLogInfo channelLogInfo) throws JsonProcessingException {
        {
            Map<String,Object> logMap = new HashMap<>();
            MsgLogUtil.dumpELKFormat(logMap, "dt", dateFormat.format(new Date()));
            MsgLogUtil.dumpELKFormat(logMap, "name", ownerName);
            MsgLogUtil.dumpELKFormatKeyTrace(logMap, isoMsg.getMsgContext());
            dumpELKFormatMessageKey(logMap, isoMsg);
            MsgLogUtil.dumpELKFormatChannel(logMap, channelLogInfo);
            MsgLogUtil.dumpELKFormatNode(logMap, isoMsg.getMsgContext());
            dumpELKFormat(logMap, isoMsg, ISOMsg.Direction.outgoing);
            return objectMapper.writeValueAsString(logMap);
        }
    }
}
