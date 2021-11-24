package io.avreen.common.util;

import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.IChannelGroupRepository;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Channel group util.
 */
public class ChannelGroupUtil {

    private static ConcurrentHashMap<String, AtomicLong> channelGroupSequenec = new ConcurrentHashMap<>();
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.util.ChannelGroupUtil");

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        String name = "hadi";
        if (!channelGroupSequenec.containsKey(name))
            channelGroupSequenec.putIfAbsent(name, new AtomicLong(1));
        AtomicLong aLong = channelGroupSequenec.get(name);
        System.out.println(aLong.incrementAndGet());

        AtomicLong aLong2 = channelGroupSequenec.get(name);
        System.out.println(aLong2.incrementAndGet());

    }


    /**
     * Exist any active connection boolean.
     *
     * @param channelGroup the channel group
     * @return the boolean
     */
    public static boolean existAnyActiveConnection(ChannelGroup channelGroup) {
        if (channelGroup == null)
            return false;
        for (Channel ch : channelGroup) {
            if (ch.isOpen() && ch.isWritable() && ch.isActive()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Select channel channel.
     *
     * @param channelGroup the channel group
     * @return the channel
     */
    public static Channel selectChannel(ChannelGroup channelGroup) {
        if (channelGroup == null)
            return null;
        ArrayList<Channel> channels = new ArrayList<>();
        for (Channel ch : channelGroup) {
            if (ch.isOpen() && ch.isWritable() && ch.isActive()) {
                channels.add(ch);
            }
        }
        if (channels.size() == 0)
            return null;
        return selectUsableChannel(channels, channelGroup.name());
    }

    /**
     * Select usable channel channel.
     *
     * @param channels the channels
     * @param groupKey the group key
     * @return the channel
     */
    public static Channel selectUsableChannel(List<Channel> channels, String groupKey) {
        if (channels.size() == 0)
            return null;
        AtomicLong sequence = channelGroupSequenec.get(groupKey);
        if (sequence == null) {
            synchronized (channelGroupSequenec) {
                AtomicLong firetSeq = new AtomicLong(0);
                sequence = channelGroupSequenec.putIfAbsent(groupKey, firetSeq);
                if (sequence == null)
                    sequence = firetSeq;
            }
        }
        long sequenceVal = sequence.incrementAndGet();
        int index = (int) (sequenceVal % channels.size());
        return channels.get(index);
    }

    /**
     * Select channel channel.
     *
     * @param channelGroupRepository the channel group repository
     * @return the channel
     */
    public static Channel selectChannel(IChannelGroupRepository channelGroupRepository) {
        ArrayList<Channel> channels = new ArrayList<>();
        for (ChannelGroup channelGroup : channelGroupRepository.getAllChannelGroups()) {
            for (Channel ch : channelGroup) {
                if (ch.isOpen() && ch.isWritable() && ch.isActive()) {
                    channels.add(ch);
                }
            }
        }
        if (channels.size() == 0)
            return null;
        return selectUsableChannel(channels, channelGroupRepository.name());
    }

    /**
     * Write and flush channel.
     *
     * @param <M>                    the type parameter
     * @param channelGroupRepository the channel group repository
     * @param msg                    the msg
     * @return the channel
     */
    public static <M> Channel writeAndFlush(IChannelGroupRepository channelGroupRepository, M msg) {
        Channel channel = ChannelGroupUtil.selectChannel(channelGroupRepository);
        if (channel != null) {
            if (!channel.isOpen() || !channel.isWritable() || !channel.isActive()) {
                if (logger.isDebugEnabled())
                    logger.debug("channel is not active for write ={} message={}", channel, msg);
                return null;
            }
            if (logger.isDebugEnabled())
                logger.debug("sending to channel={} message={}", channel, msg);
            channel.writeAndFlush(msg);
        } else {
            if (logger.isErrorEnabled())
                logger.error("not found channel to write message");
        }
        return channel;
    }

    /**
     * Gets total connections.
     *
     * @param channelGroup the channel group
     * @param checkActive  the check active
     * @return the total connections
     */
    public static int getTotalConnections(ChannelGroup channelGroup, boolean checkActive) {
        if (channelGroup == null)
            return 0;
        int connected = 0;
        for (Channel ch : channelGroup) {
            boolean okState = true;
            if (checkActive)
                okState = okState & ch.isActive();
            if (!okState)
                continue;
            if (!okState)
                continue;
            connected++;
        }
        return connected;
    }

    /**
     * Gets sessions.
     *
     * @param channelGroup the channel group
     * @return the sessions
     */
    public static String getSessions(ChannelGroup channelGroup) {
        if (channelGroup == null)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (Channel ch : channelGroup) {
            if (index > 0)
                stringBuilder.append(";");
            stringBuilder.append(ch.toString());
            stringBuilder.append(" State ");
            if (ch.isOpen())
                stringBuilder.append("O");
            if (ch.isActive())
                stringBuilder.append("A");
            if (ch.isWritable())
                stringBuilder.append("W");
            if (ch.isRegistered())
                stringBuilder.append("R");
            index++;
        }
        return stringBuilder.toString();
    }

}
