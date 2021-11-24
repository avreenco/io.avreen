package io.avreen.common.actor;


/**
 * The interface Actor.
 */
public interface IActor {
    /**
     * The constant STOPPED.
     */
    int STOPPED = 0;
    /**
     * The constant STOPPING.
     */
    int STOPPING = 1;
    /**
     * The constant STARTING.
     */
    int STARTING = 2;
    /**
     * The constant STARTED.
     */
    int STARTED = 3;
    /**
     * The constant START_FAILED.
     */
    int START_FAILED = 4;

    /**
     * The constant STOP_FAILED.
     */
    int STOP_FAILED = 5;
    /**
     * The constant INIT.
     */
    int INIT = 6;
    /**
     * The constant INIT_FAILED.
     */
    int INIT_FAILED = 7;
    /**
     * The constant INITING.
     */
    int INITING = 8;

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Sets name.
     *
     * @param name the name
     */
    void setName(String name);

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
     * Gets state.
     *
     * @return the state
     */
    int getState();

    /**
     * Gets type.
     *
     * @return the type
     */
    String getType();

    /**
     * Is enable jmx boolean.
     *
     * @return the boolean
     */
    boolean isEnableJmx();

    /**
     * Sets enable jmx.
     *
     * @param enableJmx the enable jmx
     */
    void setEnableJmx(boolean enableJmx);

}
