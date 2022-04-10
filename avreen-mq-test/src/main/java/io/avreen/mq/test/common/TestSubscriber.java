package io.avreen.mq.test.common;

import io.avreen.common.context.MsgContext;
import io.avreen.common.util.TPS;
import io.avreen.mq.api.IMsgConsumer;
import io.avreen.mq.api.IMsgSubscriber;

/**
 * The class Test.
 */
public class TestSubscriber implements Runnable {
    private IMsgSubscriber subscriber;
    private TPS tps;

    public TestSubscriber(IMsgSubscriber subscriber, TPS tps) {
        this.subscriber = subscriber;
        this.tps = tps;
    }

    @Override
    public void run() {
        subscriber.subscribe(CommonSettingProperties.instance().queueName, new IMsgConsumer<byte[]>() {
            @Override
            public void onMsg(MsgContext<byte[]> msgContext) {
                tps.tick();
                Thread.yield();
            }
        });
    }

}
