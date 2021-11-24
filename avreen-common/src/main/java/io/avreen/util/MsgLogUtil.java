package io.avreen.util;

import io.avreen.common.context.ContextKeyUtil;
import io.avreen.common.context.MsgContext;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.context.NodeInfo;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class Msg log util.
 */
public class MsgLogUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private MsgLogUtil() {

    }

    /**
     * Array list to string string.
     *
     * @param stringArrayList the string array list
     * @return the string
     */
    public static String ArrayListToString(ArrayList<String> stringArrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        int idx = 0;
        for (String s : stringArrayList) {
            stringBuilder.append(s);
            idx++;
            if (idx < stringArrayList.size())
                stringBuilder.append(",");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * Dump elk format.
     *
     * @param stringArrayList the string array list
     * @param key             the key
     * @param value           the value
     */
    public static void dumpELKFormat(ArrayList<String> stringArrayList, String key, Object value) {

        dumpELKFormat(stringArrayList, key, value, false);
    }

    /**
     * Dump elk format.
     *
     * @param stringArrayList the string array list
     * @param key             the key
     * @param value           the value
     * @param iscomposit      the iscomposit
     */
    public static void dumpELKFormat(ArrayList<String> stringArrayList, String key, Object value, boolean iscomposit) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"");
        stringBuilder.append(key);
        stringBuilder.append("\"");
        stringBuilder.append(":");
        if (!iscomposit)
            stringBuilder.append("\"");
        stringBuilder.append(value);
        if (!iscomposit)
            stringBuilder.append("\"");
        stringArrayList.add(stringBuilder.toString());

    }

    /**
     * Dump elk format channel.
     *
     * @param stringArrayList the string array list
     * @param channelLogInfo  the channel log info
     */
    public static void dumpELKFormatChannel(ArrayList<String> stringArrayList, ChannelLogInfo channelLogInfo) {
        SocketAddress remoteAddress = null;
        SocketAddress localAddress = null;
        String sessionId = null;
        if (channelLogInfo != null) {
            remoteAddress = channelLogInfo.getRemoteAddress();
            localAddress = channelLogInfo.getLocalAddress();
            sessionId = channelLogInfo.getSessionId();
        }

        if (remoteAddress != null) {
            dumpELKFormat(stringArrayList, "radr", remoteAddress);
        }
        if (localAddress != null) {
            dumpELKFormat(stringArrayList, "ladr", localAddress);
        }
        if (sessionId != null) {
            dumpELKFormat(stringArrayList, "session-id", sessionId);
        }
    }


    /**
     * Dump elk format node.
     *
     * @param stringArrayList the string array list
     * @param msgContext      the msg context
     */
    public static void dumpELKFormatNode(ArrayList<String> stringArrayList, MsgContext msgContext) {

        dumpELKFormat(stringArrayList, "node", NodeInfo.NODE_ID);
        if (msgContext != null) {
            if (msgContext.getPassingNodes().length > 1) {
                dumpELKFormat(stringArrayList, "passing-nodes", Arrays.toString(msgContext.getPassingNodes()));
            }
        }
    }

    public static String getInternalTrace(MsgContext msgContext) {
        MsgTracer msgTracer = MsgTracer.current();
        if (msgContext != null) {
            if (msgContext.getTracer() != null)
                msgTracer = msgContext.getTracer();
        }
        if (msgTracer != null) {
            String traceId = msgTracer.getTraceId();
            return traceId;
        }
        return null;
    }


    /**
     * Dump elk format key trace.
     *
     * @param stringArrayList the string array list
     * @param msgContext      the msg context
     */
    public static void dumpELKFormatKeyTrace(ArrayList<String> stringArrayList, MsgContext msgContext) {
        MsgLogUtil.dumpELKFormat(stringArrayList, "key", ContextKeyUtil.genRandomString());
        String traceId = getInternalTrace(msgContext);
        if (traceId != null)
            MsgLogUtil.dumpELKFormat(stringArrayList, "i-trace", traceId);
    }

    public static void dumpELKFormatKeyTrace(ArrayList<String> stringArrayList, String traceId) {
        MsgLogUtil.dumpELKFormat(stringArrayList, "key", ContextKeyUtil.genRandomString());
        MsgLogUtil.dumpELKFormat(stringArrayList, "i-trace", traceId);
    }

}
