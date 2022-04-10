package io.avreen.mq.api;

import java.util.HashMap;

/**
 * The class Message broker registry.
 */
public class MessageBrokerRegistry {

    /**
     * The constant msgPublisherHashMap.
     */
    public static HashMap<String, IMsgPublisher> msgPublisherHashMap = new HashMap<>();
    /**
     * The constant msgSubscriberHashMap.
     */
    public static HashMap<String, IMsgSubscriber> msgSubscriberHashMap = new HashMap<>();

    /**
     * Register publisher.
     *
     * @param name         the name
     * @param msgPublisher the msg publisher
     */
    public static void registerPublisher(String name, IMsgPublisher msgPublisher) {
        msgPublisherHashMap.putIfAbsent(name, msgPublisher);
    }

    /**
     * Gets publisher.
     *
     * @param name the name
     * @return the publisher
     */
    public static IMsgPublisher getPublisher(String name) {
        IMsgPublisher msgPublisher = msgPublisherHashMap.get(name);
        if (msgPublisher == null)
            throw new RuntimeException("can not found publisher with name=" + name);
        return msgPublisher;
    }
    public static boolean isExistPublisher(String name ) {
        IMsgPublisher msgPublisher = msgPublisherHashMap.get(name);
        return  (msgPublisher != null);
    }

    /**
     * Gets publisher.
     *
     * @param name                    the name
     * @param getFirstPublisherIfNull the get first publisher if null
     * @return the publisher
     */
    public static IMsgPublisher getPublisher(String name, boolean getFirstPublisherIfNull) {
        IMsgPublisher msgPublisher = msgPublisherHashMap.get(name);
        if (msgPublisher == null) {
            if (!getFirstPublisherIfNull)
                throw new RuntimeException("can not found publisher with name=" + name);
            msgPublisher = PublishersQueueRegistry.loadFirstPublisher(name);
            if (msgPublisher == null)
                throw new RuntimeException("can not found publisher with name=" + name + " @search first done");

        }
        return msgPublisher;
    }

    /**
     * Register subscriber.
     *
     * @param name          the name
     * @param msgSubscriber the msg subscriber
     */
    public static void registerSubscriber(String name, IMsgSubscriber msgSubscriber) {
        msgSubscriberHashMap.putIfAbsent(name, msgSubscriber);
    }

    /**
     * Gets subscriber.
     *
     * @param name the name
     * @return the subscriber
     */
    public static IMsgSubscriber getSubscriber(String name) {
        IMsgSubscriber msgSubscriber = msgSubscriberHashMap.get(name);
        if (msgSubscriber == null)
            throw new RuntimeException("can not found subscriber with name=" + name);
        return msgSubscriber;
    }
}
