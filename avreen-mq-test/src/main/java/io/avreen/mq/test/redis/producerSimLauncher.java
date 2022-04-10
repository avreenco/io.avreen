package io.avreen.mq.test.redis;

import io.avreen.mq.api.IMsgPublisher;
import io.avreen.mq.redis.RedisMsgPublisherBuilder;
import io.avreen.mq.test.common.ProducerWorker;
import io.avreen.redis.common.RedisClientConfigProperties;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * The class Test.
 */
public class producerSimLauncher implements Supplier<IMsgPublisher<String>> {

    public static void main(String[] args) throws IOException {
        ProducerWorker.start(new producerSimLauncher());
    }

    @Override
    public IMsgPublisher<String> get() {
        RedisClientConfigProperties configProperties = RedisSettingProperties.instance().getConfig();
        IMsgPublisher<String> publisher = new RedisMsgPublisherBuilder<String>(configProperties)
                .setBufferEnable(RedisSettingProperties.instance().producer_buffer_enabled)
                .setBufferQueueSize(RedisSettingProperties.instance().producer_buffer_size)
                .setPublishAsync(RedisSettingProperties.instance().producer_async_enabled)
                .setBufferSubscribeHelperThreadCount(5).build();
        return publisher;
    }
}
