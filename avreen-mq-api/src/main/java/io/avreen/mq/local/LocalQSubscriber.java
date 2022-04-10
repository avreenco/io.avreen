package io.avreen.mq.local;

import io.avreen.common.util.TPS;
import io.avreen.mq.api.IMsgConsumer;
import io.avreen.mq.api.MsgSubscriberAbstract;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The class Space base msg subscriber.
 *
 * @param <T> the type parameter
 */
class LocalQSubscriber<T> extends MsgSubscriberAbstract<T> implements LocalQSubscriberMXBean {

    private LocalQ space;
    private int consumerCount;
    private Duration waitToIncomeMessageToProcess = Duration.ofSeconds(30);

    /**
     * Instantiates a new Space base msg subscriber.
     *
     * @param space         the space
     * @param consumerCount the consumer count
     */
    public LocalQSubscriber(LocalQ space, int consumerCount) {
        this.space = space;
        this.consumerCount = consumerCount;
    }

    /**
     * Gets wait to income message to process.
     *
     * @return the wait to income message to process
     */
    public Duration getWaitToIncomeMessageToProcess() {
        return waitToIncomeMessageToProcess;
    }

    public String getWaitToProcessString() {
        return waitToIncomeMessageToProcess.toString();
    }

    /**
     * Sets wait to income message to process.
     *
     * @param waitToIncomeMessageToProcess the wait to income message to process
     */
    public void setWaitToIncomeMessageToProcess(Duration waitToIncomeMessageToProcess) {
        this.waitToIncomeMessageToProcess = waitToIncomeMessageToProcess;
    }

    @Override
    public String getSpaceName() {
        return space.getName();
    }

    @Override
    public int getQueuesCapacity() {
        return space.getQueuesCapacity();
    }

    @Override
    public int getQueuesSize() {
        return space.getQueuesSize();
    }

    public int getConsumerCount() {
        return consumerCount;
    }

    private HashMap<String, List<LocalQListenerAdaptor>> adaptorMap = new HashMap<>();

    @Override
    public void subscribeMsg(String queueName, IMsgConsumer<T> consumer) {
        TPS tps = new TPS();
        for (int idx = 0; idx < consumerCount; idx++) {
            LocalQListenerAdaptor<T> subscriberAdaptor = new LocalQListenerAdaptor<>(getName(), space, consumer, queueName, tps);
            subscriberAdaptor.setWaitToIncomeMessageToProcess(waitToIncomeMessageToProcess);
            subscriberAdaptor.setName(queueName + "-consumer-" + (idx + 1));
            subscriberAdaptor.start();
            List<LocalQListenerAdaptor> adaptorList = adaptorMap.get(queueName);
            if (adaptorList == null)
                adaptorList = new ArrayList<>();
            adaptorList.add(subscriberAdaptor);
            adaptorMap.putIfAbsent(queueName, adaptorList);

        }
    }

    @Override
    public void unsubscribe(String queueName) {
        List<LocalQListenerAdaptor> redisMessageListenerAdaptors = adaptorMap.get(queueName);
        if (redisMessageListenerAdaptors == null)
            return;
        redisMessageListenerAdaptors.forEach(redisMessageListenerAdaptor -> {
            redisMessageListenerAdaptor.stop();
        });

    }

    @Override
    public String getType() {
        return "InMemorySubscriber";
    }

}
