package io.avreen.common.cache;

import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class CacheBaseRateLimiter {
    private ICacheManager cacheManager;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.cache.CacheBaseRateLimiter");

    public CacheBaseRateLimiter(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public ICacheManager getCacheManager() {
        return cacheManager;
    }

    public void tick(String key, long cacheTimeout) {
        cacheManager.put(key, System.currentTimeMillis(), cacheTimeout);
    }

    public boolean check(String key, long callDuration) {
        Object o = cacheManager.get(key);
        if (o == null)
            return true;
        long lastCall = (long) o;
        long duration = System.currentTimeMillis() - lastCall;
        if (duration < callDuration) {
            logger.error("service call duration setting failed. service call in {} ms", duration);
            return false;
        }
        return true;
    }


}
