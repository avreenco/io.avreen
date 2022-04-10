package io.avreen.mq.test.redis;

import io.avreen.mq.api.IMsgSubscriber;
import io.avreen.mq.redis.RedisMsgSubscriberBuilder;
import io.avreen.mq.test.common.ConsumerWorker;
import io.avreen.redis.common.RedisClientConfigProperties;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * The class Test.
 */
public class consumerSimLauncher implements Supplier<IMsgSubscriber<String>> {

    public static void main(String[] args) throws IOException {
        ConsumerWorker.start(new consumerSimLauncher());
    }

    @Override
    public IMsgSubscriber<String> get() {
        RedisClientConfigProperties configProperties = RedisSettingProperties.instance().getConfig();
        RedisMsgSubscriberBuilder<String> subscriberBuilder = new RedisMsgSubscriberBuilder<>(configProperties, RedisSettingProperties.instance().consumer_thread_count);
        subscriberBuilder.setPrefetchSize(RedisSettingProperties.instance().prefetch_size);
        IMsgSubscriber<String> subscriber = subscriberBuilder.build();
        return subscriber;
    }
}
