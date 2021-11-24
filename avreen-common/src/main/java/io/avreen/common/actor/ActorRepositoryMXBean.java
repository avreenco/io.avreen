package io.avreen.common.actor;


/**
 * The interface Actor repository mx bean.
 */
public interface ActorRepositoryMXBean {

    /**
     * Destroy.
     */
    void destroy();

    /**
     * Shutdown.
     *
     * @param waitSeconds the wait seconds
     */
    void shutdown(int waitSeconds);

}
