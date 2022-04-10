package io.avreen.mq.local;


import io.avreen.mq.api.IMsgPublisher;
import io.avreen.mq.api.MessageBrokerRegistry;

import java.time.Duration;

/**
 * The class Space base msg publisher builder.
 *
 * @param <M> the type parameter
 */
public class LocalQPublisherBuilder<M> {

    private String spaceURI;
    private int queuesCapacity = 0;
    private Duration offerTimeout = Duration.ofSeconds(2);
    private String name;

    /**
     * Instantiates a new Space base msg publisher builder.
     *
     * @param spaceURI the space uri
     */
    public LocalQPublisherBuilder(String spaceURI) {
        this.spaceURI = spaceURI;
    }

    /**
     * Instantiates a new Space base msg publisher builder.
     */
    public LocalQPublisherBuilder() {
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
     */
    public void setName(String name) {
        this.name = name;
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
    public LocalQPublisherBuilder<M> setSpaceURI(String spaceURI) {
        this.spaceURI = spaceURI;
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
    public LocalQPublisherBuilder<M> setQueuesCapacity(int queuesCapacity) {
        this.queuesCapacity = queuesCapacity;
        return this;
    }

    /**
     * Gets offer timeout.
     *
     * @return the offer timeout
     */
    public Duration getOfferTimeout() {
        return offerTimeout;
    }

    /**
     * Sets offer timeout.
     *
     * @param offerTimeout the offer timeout
     * @return the offer timeout
     */
    public LocalQPublisherBuilder<M> setOfferTimeout(Duration offerTimeout) {
        this.offerTimeout = offerTimeout;
        return this;
    }

    /**
     * Build msg publisher.
     *
     * @return the msg publisher
     */
    public IMsgPublisher<M> build() {
        if (getName() == null)
            setName("space");
        if (spaceURI == null)
            spaceURI = getName() + ".space";

        LocalQPublisher spaceBaseMsgPublisher = new LocalQPublisher(LocalQFactory.getSpace(spaceURI, () -> new LocalQ(spaceURI, queuesCapacity)));
        spaceBaseMsgPublisher.setOfferTimeout(offerTimeout);
        spaceBaseMsgPublisher.setName(getName());
        MessageBrokerRegistry.registerPublisher(getName(), spaceBaseMsgPublisher);
        spaceBaseMsgPublisher.start();
        return spaceBaseMsgPublisher;
    }
}
