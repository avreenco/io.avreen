package io.avreen.mq.redis;

import io.avreen.common.actor.ActorBaseMXBean;

/**
 * The interface Redis message broker component mx bean.
 */
public interface RedisMessageBrokerComponentMXBean extends ActorBaseMXBean {
    /**
     * Gets session client id.
     *
     * @return the session client id
     */
    long getSessionClientID();

    /**
     * Gets session info.
     *
     * @return the session info
     */
    String getSessionInfo();

    /**
     * Gets client session name.
     *
     * @return the client session name
     */
    String getClientSessionName();

    /**
     * Gets bind redis address.
     *
     * @return the bind redis address
     */
    String getBindRedisAddress();

    /**
     * Gets bind redis port.
     *
     * @return the bind redis port
     */
    String getBindRedisPort();


}
