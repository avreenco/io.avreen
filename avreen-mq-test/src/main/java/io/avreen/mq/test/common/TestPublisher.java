package io.avreen.mq.test.common;

import io.avreen.common.context.MsgContext;
import io.avreen.common.util.TPS;
import io.avreen.mq.api.IMsgPublisher;

import java.util.Random;

/**
 * The class Test.
 */
public class TestPublisher implements Runnable {
    private IMsgPublisher publisher;
    private TPS tps;

    public TestPublisher(IMsgPublisher publisher, TPS tps) {
        this.publisher = publisher;
        this.tps = tps;
    }

    private byte[] buildBody() {
        byte[] body = new byte[CommonSettingProperties.instance().body_size];
        new Random().nextBytes(body);
        return body;
    }


    @Override
    public void run() {
        byte[] test_body = buildBody();
        String qname = CommonSettingProperties.instance().queueName;
        for (; ; ) {
            try {
                MsgContext<byte[]> msgContext = new MsgContext<>("test", test_body);
                publisher.publish(qname, msgContext);
                tps.tick();
                Thread.yield();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
