package io.avreen.mq.test.redis;

import io.avreen.common.util.SystemPropUtil;
import io.avreen.redis.common.RedisClientConfigProperties;
import io.lettuce.core.protocol.ProtocolVersion;

class RedisSettingProperties {

    private static RedisSettingProperties ins = new RedisSettingProperties();

    private RedisSettingProperties() {

    }

    public static RedisSettingProperties instance() {
        return ins;
    }

    public String redis_uri = SystemPropUtil.get("test.redis-uri", "redis-sentinel://SuperS3cr3tP455@172.16.10.89:26400,172.16.10.90:26400,172.16.10.91:26400#redisPubsub");
    public int prefetch_size = SystemPropUtil.getInt("test.prefetch-size", 10);
    public int consumer_thread_count = SystemPropUtil.getInt("test.consumer-thread-count", 7);
    public int producer_buffer_size = SystemPropUtil.getInt("test.producer-buffer-size", 10);
    public boolean producer_buffer_enabled = SystemPropUtil.getBoolean("test.producer-buffer-enabled", true);
    public boolean producer_async_enabled = SystemPropUtil.getBoolean("test.producer-async-enabled", false);

    public RedisClientConfigProperties getConfig() {
        RedisClientConfigProperties configProperties = new RedisClientConfigProperties();
        configProperties.setName("test-config");
        configProperties.setRedisUri(redis_uri);
        //configProperties.setTcpNoDelay(true);
        configProperties.setProtocolVersion(ProtocolVersion.newestSupported());
        return configProperties;
    }

}
