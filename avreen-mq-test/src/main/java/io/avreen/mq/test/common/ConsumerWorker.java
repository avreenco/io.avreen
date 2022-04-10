package io.avreen.mq.test.common;

import io.avreen.common.util.TPS;
import io.avreen.mq.api.IMsgSubscriber;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * The class Test.
 */
public class ConsumerWorker {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    private static TPS tps = new TPS();

    public static void start(Supplier<IMsgSubscriber<String>> msgPublisherSupplier ) throws IOException {
        TPSReporter tpsReporter = new TPSReporter(tps);
        System.out.println("press any key to start consumer");
        System.in.read();
        tpsReporter.start();
        startSubscriber(msgPublisherSupplier);
    }

    private static void startSubscriberSingleNode(Supplier<IMsgSubscriber<String>> msgPublisherSupplier) {
        IMsgSubscriber<String> subscriber = msgPublisherSupplier.get();
        TestSubscriber testSubscriber = new TestSubscriber(subscriber, tps);
        new Thread(testSubscriber).start();
    }

    private static void startSubscriber(Supplier<IMsgSubscriber<String>> msgPublisherSupplier ) {
        for (int i = 0; i < CommonSettingProperties.instance().consumer_node_count; i++) {
            startSubscriberSingleNode(msgPublisherSupplier);
        }
    }
}
