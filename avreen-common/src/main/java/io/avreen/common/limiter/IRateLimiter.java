package io.avreen.common.limiter;

import io.avreen.common.actor.IActor;

/**
 * The interface Rate limiter.
 */
public interface IRateLimiter extends IActor {
    String getName();

    /**
     * Try acquire boolean.
     *
     * @return the boolean
     */
    boolean tryAcquire(String   spaceKey);
}
