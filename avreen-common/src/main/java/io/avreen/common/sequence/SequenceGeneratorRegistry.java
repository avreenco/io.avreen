package io.avreen.common.sequence;

import java.util.HashMap;

/**
 * The class Cache manager registry.
 */
public class SequenceGeneratorRegistry {

    /**
     * The constant cacheManagerHashMap.
     */
    public static HashMap<String, ISequenceGenerator> sequenceGeneratorHashMap = new HashMap<>();

    /**
     * Register cache manager.
     *
     * @param name         the name
     * @param msgPublisher the msg publisher
     */
    public static void registerSequenceGenerator(String name, ISequenceGenerator msgPublisher) {
        sequenceGeneratorHashMap.putIfAbsent(name, msgPublisher);
    }

    /**
     * Gets cache manager.
     *
     * @param name the name
     * @return the cache manager
     */
    public static ISequenceGenerator getSequenceGenerator(String name) {
        return getSequenceGenerator(name, true);
    }

    /**
     * Gets cache manager.
     *
     * @param name            the name
     * @param throwIfNotExist the throw if not exist
     * @return the cache manager
     */
    public static ISequenceGenerator getSequenceGenerator(String name, boolean throwIfNotExist) {
        ISequenceGenerator msgPublisher = sequenceGeneratorHashMap.get(name);
        if (msgPublisher == null && throwIfNotExist)
            throw new RuntimeException("can not found sequence manager with name=" + name);
        return msgPublisher;
    }

}
