package io.avreen.common.actor;


/**
 * The interface Actor.
 */
public interface IActor {
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
    ActorState getState();

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
