package io.avreen.iso8583.util;

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

    /**
     * Instantiates a new Iso msg log util.
     */
    public ISOMsgLogUtil() {

    }

    /**
     * Instantiates a new Iso msg log util.
     *
     * @param excludeField the exclude field
     * @param includeField the include field
     */
    public ISOMsgLogUtil(Set<Integer> excludeField, Set<Integer> includeField) {
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

    private void dumpELKFormat(ArrayList<String> stringArrayList, ISOMsg isoMsg, ISOMsg.Direction direction) {

        if (direction != null) {
            MsgLogUtil.dumpELKFormat(stringArrayList, "dir", direction.equals(ISOMsg.Direction.incoming) ? "in" : "out");
        }
        if (isoMsg.isReject()) {
            MsgLogUtil.dumpELKFormat(stringArrayList, "rjc", isoMsg.getRejectCode());
            if (isoMsg.getRejectBuffer() != null) {
                MsgLogUtil.dumpELKFormat(stringArrayList, "rjb", ISOUtil.hexString(isoMsg.getRejectBuffer()));
            }
        }


        if (isoMsg.getISOHeader() != null) {
            if (logHeader)
                MsgLogUtil.dumpELKFormat(stringArrayList, "header", ISOUtil.hexString(isoMsg.getISOHeader()));
        }


        if (!isoMsg.isReject()) {
            Map map = isoMsg.getChildren();
            Set fieldSet = map.keySet();
            if (includeField != null && includeField.size() > 0)
                fieldSet = includeField;

            ArrayList<String> bodyStringList = new ArrayList<>();
            for (Object fno : fieldSet) {
                int fieldNo = (int) fno;
                if (excludeField != null && excludeField.contains(fieldNo))
                    continue;
                if (!isoMsg.hasField(fieldNo))
                    continue;
                ISOComponent isoComponent = isoMsg.getComponent(fieldNo);
                if (isoComponent.getValue() == null)
                    continue;

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\"i");
                stringBuilder.append(fno);
                stringBuilder.append("\"");
                stringBuilder.append(":");
                stringBuilder.append("\"");
                ISOComponentDumper isoComponentDumper = DumpUtil.getIsoComponentDumper(isoComponent);
                Object valueString = isoComponentDumper.convertToString(isoComponent);
                if (valueString != null)
                    stringBuilder.append(valueString);
                stringBuilder.append("\"");
                bodyStringList.add(stringBuilder.toString());
            }
            String bodyString = MsgLogUtil.ArrayListToString(bodyStringList);
            MsgLogUtil.dumpELKFormat(stringArrayList, "body", bodyString, true);
        }
    }

    private void dumpELKFormatMessageKey(ArrayList<String> stringArrayList, ISOMsg isoMsg) {

        if (messageTraceField == null)
            return;
        ArrayList<String> msgField = new ArrayList<>();
        for (Integer f : messageTraceField) {
            if (isoMsg.hasField(f)) {
                if (isoMsg.getString(f) != null)
                    msgField.add(isoMsg.getString(f));
            }
        }
        if (msgField.size() == 0)
            return;
        String key = String.join(messageTraceFieldDelimiter, msgField);
        MsgLogUtil.dumpELKFormat(stringArrayList, "m-trace", key);
    }

    /**
     * Build incoming iso msg log string.
     *
     * @param ownerName      the owner name
     * @param isoMsg         the iso msg
     * @param channelLogInfo the channel log info
     * @return the string
     */
    public String buildIncomingISOMsgLog(String ownerName, ISOMsg isoMsg, ChannelLogInfo channelLogInfo) {
        {
            ArrayList<String> strings = new ArrayList<>();
            MsgLogUtil.dumpELKFormat(strings, "dt", dateFormat.format(new Date()));
            MsgLogUtil.dumpELKFormat(strings, "name", ownerName);
            MsgLogUtil.dumpELKFormatKeyTrace(strings, isoMsg.getMsgContext());
            dumpELKFormatMessageKey(strings, isoMsg);
            MsgLogUtil.dumpELKFormatChannel(strings, channelLogInfo);
            MsgLogUtil.dumpELKFormatNode(strings, isoMsg.getMsgContext());
            dumpELKFormat(strings, isoMsg, ISOMsg.Direction.incoming);
            return MsgLogUtil.ArrayListToString(strings);
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
    public String buildOutgoingISOMsgLog(String ownerName, ISOMsg isoMsg, ChannelLogInfo channelLogInfo) {
        {
            ArrayList<String> strings = new ArrayList<>();
            MsgLogUtil.dumpELKFormat(strings, "dt", dateFormat.format(new Date()));
            MsgLogUtil.dumpELKFormat(strings, "name", ownerName);
            MsgLogUtil.dumpELKFormatKeyTrace(strings, isoMsg.getMsgContext());
            dumpELKFormatMessageKey(strings, isoMsg);
            MsgLogUtil.dumpELKFormatChannel(strings, channelLogInfo);
            MsgLogUtil.dumpELKFormatNode(strings, isoMsg.getMsgContext());
            dumpELKFormat(strings, isoMsg, ISOMsg.Direction.outgoing);
            return MsgLogUtil.ArrayListToString(strings);
        }
    }
}
