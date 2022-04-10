package io.avreen.mq.local;


import io.avreen.mq.api.IMsgSubscriber;
import io.avreen.mq.api.MessageBrokerRegistry;

import java.time.Duration;

/**
 * The class Space base msg subscriber builder.
 *
 * @param <M> the type parameter
 */
public class LocalQSubscriberBuilder<M> {
    private String spaceURI;
    private int consumerCount = 1;
    private int queuesCapacity = 0;
    private Duration waitToIncomeMessageToProcess = Duration.ofSeconds(30);
    private String name;

    /**
     * Instantiates a new Space base msg subscriber builder.
     *
     * @param spaceURI the space uri
     */
    public LocalQSubscriberBuilder(String spaceURI) {
        this.spaceURI = spaceURI;
    }

    /**
     * Instantiates a new Space base msg subscriber builder.
     */
    public LocalQSubscriberBuilder() {
    }

    /**
     * Gets wait to income message to process.
     *
     * @return the wait to income message to process
     */
    public Duration getWaitToIncomeMessageToProcess() {
        return waitToIncomeMessageToProcess;
    }

    /**
     * Sets wait to income message to process.
     *
     * @param waitToIncomeMessageToProcess the wait to income message to process
     * @return the wait to income message to process
     */
    public LocalQSubscriberBuilder<M> setWaitToIncomeMessageToProcess(Duration waitToIncomeMessageToProcess) {
        this.waitToIncomeMessageToProcess = waitToIncomeMessageToProcess;
        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public LocalQSubscriberBuilder<M> setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets space uri.
     *
     * @return the space uri
     */
    public String getSpaceURI() {
        return spaceURI;
    }

    /**
     * Sets space uri.
     *
     * @param spaceURI the space uri
     * @return the space uri
     */
    public LocalQSubscriberBuilder<M> setSpaceURI(String spaceURI) {
        this.spaceURI = spaceURI;
        return this;
    }

    /**
     * Gets consumer count.
     *
     * @return the consumer count
     */
    public int getConsumerCount() {
        return consumerCount;
    }

    /**
     * Sets consumer count.
     *
     * @param consumerCount the consumer count
     * @return the consumer count
     */
    public LocalQSubscriberBuilder<M> setConsumerCount(int consumerCount) {
        this.consumerCount = consumerCount;
        return this;
    }

    /**
     * Gets queues capacity.
     *
     * @return the queues capacity
     */
    public int getQueuesCapacity() {
        return queuesCapacity;
    }

    /**
     * Sets queues capacity.
     *
     * @param queuesCapacity the queues capacity
     * @return the queues capacity
     */
    public LocalQSubscriberBuilder<M> setQueuesCapacity(int queuesCapacity) {
        this.queuesCapacity = queuesCapacity;
        return this;
    }

    /**
     * Build msg subscriber.
     *
     * @return the msg subscriber
     */
    public IMsgSubscriber<M> build() {
        if (getName() == null)
            setName("space");
        if (spaceURI == null)
            spaceURI = getName() + ".space";

        LocalQSubscriber spaceBaseMsgSubscriber = new LocalQSubscriber(LocalQFactory.getSpace(spaceURI, () -> new LocalQ(spaceURI, queuesCapacity)), consumerCount);
        spaceBaseMsgSubscriber.setName(getName());
        spaceBaseMsgSubscriber.setWaitToIncomeMessageToProcess(waitToIncomeMessageToProcess);
        MessageBrokerRegistry.registerSubscriber(getName(), spaceBaseMsgSubscriber);
        spaceBaseMsgSubscriber.start();
        return spaceBaseMsgSubscriber;

    }
}
