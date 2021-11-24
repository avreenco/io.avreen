package io.avreen.common.netty;

import io.netty.channel.group.ChannelGroup;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The class Channel group repository.
 */
public class ChannelGroupRepository implements IChannelGroupRepository {

    private static final AtomicInteger nextId = new AtomicInteger();
    private String name;
    private Map<String, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Channel group repository.
     *
     * @param name the name
     */
    public ChannelGroupRepository(String name) {
        this.name = name;
    }

    /**
     * Instantiates a new Channel group repository.
     */
    public ChannelGroupRepository() {
        this.name = "channelGroupRepo-0x" + Integer.toHexString(nextId.incrementAndGet());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void put(String connectorID, ChannelGroup channelGroup) {
        channelGroupMap.put(connectorID, channelGroup);
    }

    @Override
    public Collection<ChannelGroup> getAllChannelGroups() {
        return channelGroupMap.values();
    }
}
