package io.avreen.mq.api;

import io.avreen.common.actor.ActorBase;

/**
 * The class Msg subscriber abstract.
 *
 * @param <M> the type parameter
 */
public abstract class MsgSubscriberAbstract<M> extends ActorBase implements IMsgSubscriber<M>, MsgSubscriberAbstractMXBean {

    @Override
    public void subscribe(String queueName, IMsgConsumer<M> consumer) {
        subscribeMsg(queueName, consumer);
    }

    /**
     * Subscribe msg.
     *
     * @param queueName the queue name
     * @param consumer  the consumer
     */
    public abstract void subscribeMsg(String queueName, IMsgConsumer<M> consumer);


    /**
     * Sets subscriber name.
     *
     * @param subscriberName the subscriber name
     */
    public void setSubscriberName(String subscriberName) {
        setName(subscriberName);
    }

    @Override
    public String toString() {
        return "Subscriber." + getName();
    }

    @Override
    protected void startService() throws Throwable {
    }

    @Override
    protected void stopService() throws Throwable {
    }

    @Override
    public String stopSubscribe(String queueName) {
        unsubscribe(queueName);
        return "OK";
    }

}
