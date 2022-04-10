package io.avreen.mq.local;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The class Space factory.
 */
class LocalQFactory {
    private static ConcurrentHashMap<String, LocalQ> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * Gets space.
     *
     * @param spaceName    the space name
     * @param spaceBuilder the space builder
     * @return the space
     */
    public static LocalQ getSpace(String spaceName, SpaceBuilder spaceBuilder) {
        if (spaceName == null)
            spaceName = "system.space";
        if (concurrentHashMap.containsKey(spaceName))
            return concurrentHashMap.get(spaceName);
        synchronized (concurrentHashMap) {
            if (concurrentHashMap.containsKey(spaceName))
                return concurrentHashMap.get(spaceName);
            LocalQ tSpace = spaceBuilder.create();
            LocalQ oldValue = concurrentHashMap.putIfAbsent(spaceName, tSpace);
            if (oldValue != null)
                return oldValue;
            return tSpace;
        }
    }

    /**
     * The interface Space builder.
     */
    public interface SpaceBuilder {
        /**
         * Create in memory queue space.
         *
         * @return the in memory queue space
         */
        LocalQ create();
    }
}
