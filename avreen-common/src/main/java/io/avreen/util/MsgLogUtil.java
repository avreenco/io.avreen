package io.avreen.util;

import io.avreen.common.context.ContextKeyUtil;
import io.avreen.common.context.MsgContext;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.context.NodeInfo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

/**
 * The class Msg log util.
 */
public class MsgLogUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private MsgLogUtil() {

    }

    public static void dumpELKFormat(Map<String,Object> logMap, String key, Object value) {

        if(value!=null)
            logMap.put(key , value);
    }

    public static void dumpELKFormatChannel(Map<String,Object> logMap, ChannelLogInfo channelLogInfo) {
        SocketAddress remoteAddress = null;
        SocketAddress localAddress = null;
        String sessionId = null;
        if (channelLogInfo != null) {
            remoteAddress = channelLogInfo.getRemoteAddress();
            localAddress = channelLogInfo.getLocalAddress();
            sessionId = channelLogInfo.getSessionId();
        }

        if (remoteAddress != null) {
            InetSocketAddress socketAddress = (InetSocketAddress) remoteAddress;
            dumpELKFormat(logMap, "rip", socketAddress.getHostString());
            dumpELKFormat(logMap, "rpt", socketAddress.getPort());
            //dumpELKFormat(stringArrayList, "radr", remoteAddress);
        }
        if (localAddress != null) {
//            dumpELKFormat(stringArrayList, "ladr", localAddress);
            InetSocketAddress socketAddress = (InetSocketAddress) localAddress;
            dumpELKFormat(logMap, "lip", socketAddress.getHostString());
            dumpELKFormat(logMap, "lpt", socketAddress.getPort());
        }
        if (sessionId != null) {
            dumpELKFormat(logMap, "session-id", sessionId);
        }
    }


    public static void dumpELKFormatNode(Map<String,Object> logMap, MsgContext msgContext) {

        dumpELKFormat(logMap, "node", NodeInfo.NODE_ID);
        if (msgContext != null) {
            if (msgContext.getPassingNodes().length > 1) {
                dumpELKFormat(logMap, "passing-nodes", Arrays.toString(msgContext.getPassingNodes()));
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


    public static void dumpELKFormatKeyTrace(Map<String,Object> logMap, MsgContext msgContext) {
        MsgLogUtil.dumpELKFormat(logMap, "key", ContextKeyUtil.genRandomString());
        String traceId = getInternalTrace(msgContext);
        if (traceId != null)
            MsgLogUtil.dumpELKFormat(logMap, "i-trace", traceId);
    }

    public static void dumpELKFormatKeyTrace(Map<String,Object> logMap, String traceId) {
        MsgLogUtil.dumpELKFormat(logMap, "key", ContextKeyUtil.genRandomString());
        MsgLogUtil.dumpELKFormat(logMap, "i-trace", traceId);
    }

}
