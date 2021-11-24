package io.avreen.common.actor;


import java.util.Date;

/**
 * The interface Actor base mx bean.
 */
public interface ActorBaseMXBean {

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Start string.
     *
     * @return the string
     */
    String start();

    /**
     * Stop string.
     *
     * @return the string
     */
    String stop();

    /**
     * Gets state as string.
     *
     * @return the state as string
     */
    String getStateAsString();

    /**
     * Is running boolean.
     *
     * @return the boolean
     */
    boolean isRunning();

    /**
     * Gets create time.
     *
     * @return the create time
     */
    Date getCreateTime();

    /**
     * Gets last started time.
     *
     * @return the last started time
     */
    Date getLastStartedTime();

    /**
     * Gets type.
     *
     * @return the type
     */
    String getType();

    /**
     * Gets last stopped time.
     *
     * @return the last stopped time
     */
    Date getLastStoppedTime();


}
