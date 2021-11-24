package io.avreen.common.netty;

import io.avreen.common.context.ContextKeyUtil;
import io.netty.channel.Channel;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The class Session log util.
 */
public class SessionLogUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Instantiates a new Session log util.
     */
    public SessionLogUtil() {

    }


    private static String ArrayListToString(ArrayList<String> stringArrayList) {
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

    private static void dumpELKFormat(ArrayList<String> stringArrayList, String key, Object value) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"");
        stringBuilder.append(key);
        stringBuilder.append("\"");
        stringBuilder.append(":");
        stringBuilder.append("\"");
        stringBuilder.append(value);
        stringBuilder.append("\"");
        stringArrayList.add(stringBuilder.toString());

    }

    private static void dumpELKFormat(ArrayList<String> stringArrayList, io.netty.channel.Channel channel) {
        if (channel != null)
            dumpELKFormat(stringArrayList, "channel", channel);
        if (channel != null && channel.remoteAddress() != null) {
            dumpELKFormat(stringArrayList, "radr", channel.remoteAddress());
        }
        if (channel != null && channel.localAddress() != null) {
            dumpELKFormat(stringArrayList, "ladr", channel.localAddress());
        }
    }

    private static void dumpELKFormat(ArrayList<String> stringArrayList, SocketAddress remoteAddress, SocketAddress localAddress) {
        if (remoteAddress != null) {
            dumpELKFormat(stringArrayList, "radr", remoteAddress);
        }
        if (localAddress != null) {
            dumpELKFormat(stringArrayList, "ladr", localAddress);
        }
    }


    /**
     * Build session event log string.
     *
     * @param ownerName the owner name
     * @param event     the event
     * @param channel   the channel
     * @param error     the error
     * @return the string
     */
    public String buildSessionEventLog(String ownerName, String event, Channel channel, Throwable error) {
        {
            ArrayList<String> strings = new ArrayList<>();
            dumpELKFormat(strings, "id", ContextKeyUtil.genRandomString());
            dumpELKFormat(strings, "dt", dateFormat.format(new Date()));
            dumpELKFormat(strings, "name", ownerName);
            dumpELKFormat(strings, "event", event);
            String sessionID = channel.id().asLongText();
            dumpELKFormat(strings, channel);
            if (error != null)
                dumpELKFormat(strings, "error", error.getMessage());

            return ArrayListToString(strings);
        }
    }

    /**
     * Build session event log string.
     *
     * @param ownerName the owner name
     * @param event     the event
     * @param channel   the channel
     * @return the string
     */
    public String buildSessionEventLog(String ownerName, String event, Channel channel) {
        {

            return buildSessionEventLog(ownerName, event, channel, null);
        }
    }

    /**
     * Build session event log string.
     *
     * @param ownerName     the owner name
     * @param event         the event
     * @param remoteAddress the remote address
     * @param localAddress  the local address
     * @return the string
     */
    public String buildSessionEventLog(String ownerName, String event, SocketAddress remoteAddress, SocketAddress localAddress) {
        ArrayList<String> strings = new ArrayList<>();
        dumpELKFormat(strings, "id", ContextKeyUtil.genRandomString());
        dumpELKFormat(strings, "dt", dateFormat.format(new Date()));
        dumpELKFormat(strings, "name", ownerName);
        dumpELKFormat(strings, "event", event);
        dumpELKFormat(strings, remoteAddress, localAddress);
//        if (error != null)
//            dumpELKFormat(strings, "error", error.getMessage());
        return ArrayListToString(strings);
    }

}
