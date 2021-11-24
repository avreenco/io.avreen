package io.avreen.common.netty;

import io.avreen.common.context.MsgContext;
import io.avreen.common.context.MsgTracer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;


/**
 * The class Netty msg context util.
 */
public class NettyMsgContextUtil {

    private static final String CHANNEL_KEY = "CHANNEL_KEY";

    /**
     * Create msg context msg context.
     *
     * @param <M>       the type parameter
     * @param channel   the channel
     * @param creator the creator
     * @param isoMsg    the iso msg
     * @param msgTracer the msg tracer
     * @return the msg context
     */
    public static <M> MsgContext<M> createMsgContext(Channel channel, String creator, M isoMsg, MsgTracer msgTracer) {
        MsgContext<M> msgContext = new MsgContext<M>(creator, isoMsg, msgTracer);
        msgContext.put(CHANNEL_KEY, channel.id());
        return msgContext;
    }

    /**
     * Contain channel boolean.
     *
     * @param msgContext the msg context
     * @return the boolean
     */
    public static boolean containChannel(MsgContext msgContext) {
        if (!msgContext.containKey(CHANNEL_KEY))
            return false;
        return true;

    }

    /**
     * Sets channel.
     *
     * @param msgContext the msg context
     * @param channelId  the channel id
     */
    public static void setChannel(MsgContext msgContext, ChannelId channelId) {
        msgContext.put(CHANNEL_KEY, channelId);
    }

    /**
     * Gets channel.
     *
     * @param msgContext the msg context
     * @return the channel
     */
    public static ChannelId getChannel(MsgContext msgContext) {
        if (!msgContext.containKey(CHANNEL_KEY))
            return null;
        return (ChannelId) msgContext.get(CHANNEL_KEY);
    }


}
