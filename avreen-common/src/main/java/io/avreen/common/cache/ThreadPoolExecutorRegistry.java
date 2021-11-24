package io.avreen.common.cache;

import java.util.HashMap;
import java.util.concurrent.Executor;

/**
 * The class Thread pool executor registry.
 */
public class ThreadPoolExecutorRegistry {

    /**
     * The constant cacheManagerHashMap.
     */
    public static HashMap<String, Executor> cacheManagerHashMap = new HashMap<>();

    /**
     * Register executor.
     *
     * @param name         the name
     * @param msgPublisher the msg publisher
     */
    public static void registerExecutor(String name, Executor msgPublisher) {
        cacheManagerHashMap.putIfAbsent(name, msgPublisher);
    }

    /**
     * Gets executor.
     *
     * @param name the name
     * @return the executor
     */
    public static Executor getExecutor(String name) {
        Executor msgPublisher = cacheManagerHashMap.get(name);
        if (msgPublisher == null)
            throw new RuntimeException("can not found thread pool with name=" + name);
        return msgPublisher;
    }

}
