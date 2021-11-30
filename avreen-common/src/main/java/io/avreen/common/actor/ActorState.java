package io.avreen.common.actor;


public enum ActorState {
    STOPPED(0),
    /**
     * The constant STOPPING.
     */
    STOPPING(1),
    /**
     * The constant STARTING.
     */
    STARTING(2),
    /**
     * The constant STARTED.
     */
    STARTED(3),
    /**
     * The constant START_FAILED.
     */
    START_FAILED(4),

    /**
     * The constant STOP_FAILED.
     */
    STOP_FAILED(5),
    /**
     * The constant INIT.
     */
    INIT(6),
    /**
     * The constant INIT_FAILED.
     */
    INIT_FAILED(7),
    /**
     * The constant INITING.
     */
    INITING(8);

    private final int value;

    ActorState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
