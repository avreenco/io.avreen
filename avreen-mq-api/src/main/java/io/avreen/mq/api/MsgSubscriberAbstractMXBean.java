
package io.avreen.mq.api;

import io.avreen.common.actor.ActorBaseMXBean;

/**
 * The interface Msg subscriber abstract mx bean.
 *
 * @param <M> the type parameter
 */
public interface MsgSubscriberAbstractMXBean<M> extends ActorBaseMXBean {

    /**
     * Stop subscribe string.
     *
     * @param queueName the queue name
     * @return the string
     */
    String stopSubscribe(String queueName);
}
