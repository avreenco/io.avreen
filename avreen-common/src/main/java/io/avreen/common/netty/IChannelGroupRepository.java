package io.avreen.common.netty;

import io.netty.channel.group.ChannelGroup;

import java.util.Collection;

/**
 * The interface Channel group repository.
 */
public interface IChannelGroupRepository {
    /**
     * Name string.
     *
     * @return the string
     */
    String name();

    /**
     * Put.
     *
     * @param connectorID  the connector id
     * @param channelGroup the channel group
     */
    void put(String connectorID, ChannelGroup channelGroup);

    /**
     * Gets all channel groups.
     *
     * @return the all channel groups
     */
    Collection<ChannelGroup> getAllChannelGroups();
}
