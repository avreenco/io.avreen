package io.avreen.mq.test.common;

import io.avreen.common.util.TPS;
import io.avreen.mq.api.IMsgPublisher;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * The class Test.
 */
public class ProducerWorker {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    private static TPS tps = new TPS();

    public static void start(Supplier<IMsgPublisher<String>> msgPublisherSupplier) throws IOException {
        TPSReporter tpsReporter = new TPSReporter(tps);
        System.out.println("press any key to start producer");
        System.in.read();
        tpsReporter.start();
        startPublisher(msgPublisherSupplier);
    }

    private static void startPublisherSingleNode(Supplier<IMsgPublisher<String>> msgPublisherSupplier) {
        IMsgPublisher<String> publisher = msgPublisherSupplier.get();
        TestPublisher testPublisher = new TestPublisher(publisher, tps);
        new Thread(testPublisher).start();
    }

    private static void startPublisher(Supplier<IMsgPublisher<String>> msgPublisherSupplier) {
        for (int i = 0; i < CommonSettingProperties.instance().producer_node_count; i++) {
            startPublisherSingleNode(msgPublisherSupplier);
        }
    }
}
