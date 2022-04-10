package io.avreen.http.bomber;

import io.avreen.common.cache.CacheBaseRateLimiter;

class BomberCacheEntry
{
    private CacheBaseRateLimiter   cacheBaseRateLimiter ;
    private String   key ;
    private HttpMsgBomberProperties bomberProperties;

    public BomberCacheEntry(String key,CacheBaseRateLimiter cacheBaseRateLimiter ,  HttpMsgBomberProperties bomberProperties) {
        this.cacheBaseRateLimiter = cacheBaseRateLimiter;
        this.key = key;
        this.bomberProperties = bomberProperties;
    }

    public CacheBaseRateLimiter getCacheBaseRateLimiter() {
        return cacheBaseRateLimiter;
    }

    public String getKey() {
        return key;
    }

    public HttpMsgBomberProperties getBomberProperties() {
        return bomberProperties;
    }
}
