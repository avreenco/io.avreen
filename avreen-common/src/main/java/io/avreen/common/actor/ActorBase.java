package io.avreen.common.actor;


import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.Date;
import java.util.UUID;

import static io.avreen.common.actor.ActorState.*;


/**
 * The class Actor base.
 */
public abstract class ActorBase extends NotificationBroadcasterSupport implements IActor, ActorBaseMXBean, IDestroySupport {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.actor.ActorBase");

    private ActorState state = null;
    private String name;
    private Date createTime = null;
    private Date lastStartedTime = null;
    private Date lastStoppedTime = null;
    private boolean enableJmx = true;

    /**
     * Instantiates a new Actor base.
     */
    public ActorBase() {
        super();
    }

    public boolean isEnableJmx() {
        return enableJmx;
    }

    public void setEnableJmx(boolean enableJmx) {
        this.enableJmx = enableJmx;
    }

    /**
     * Is build state boolean.
     *
     * @return the boolean
     */
    public boolean isBuildState() {
        return state != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.name != null)
            throw new RuntimeException("actor name set before. old name=" + name);
        this.name = name;
    }

    /**
     * Gets name set default if null.
     *
     * @return the name set default if null
     */
    public String getNameSetDefaultIfNull() {
        if (name == null)
            name = this.getClass().getName() + "." + UUID.randomUUID().toString();
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * To name type string.
     *
     * @return the string
     */
    public String toNameType() {
        return "{[name=" + getName() + " ,  type=" + getType() + "]";
    }


    private void relax() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            /* nothing */
        }
    }

    /**
     * Gets notification user data.
     *
     * @return the notification user data
     */
    protected String getNotificationUserData() {
        return getClass().getName();
    }

    private void sendStartNotification() {
        if (getName() == null)
            return;
        Notification notification = new Notification("info", getType() + "." + getName(), 1, "start");
        notification.setTimeStamp(System.currentTimeMillis());
        notification.setUserData(getNotificationUserData());
        sendNotification(notification);

    }

    private void sendInProgressNotification() {
        if (getName() == null)
            return;
        Notification notification = new Notification("info", getType() + "." + getName(), 1, getStateAsString());
        notification.setTimeStamp(System.currentTimeMillis());
        notification.setUserData(getNotificationUserData());
        sendNotification(notification);

    }

    private void sendStartFailNotification() {
        if (getName() == null)
            return;
        Notification notification = new Notification("error", getType() + "." + getName(), 1, "start fail");
        notification.setTimeStamp(System.currentTimeMillis());
        notification.setUserData(getNotificationUserData());
        sendNotification(notification);

    }

    private void sendStopNotification() {
        if (getName() == null)
            return;

        Notification notification = new Notification("warn", getType() + "." + getName(), 1, "stop");
        notification.setTimeStamp(System.currentTimeMillis());
        notification.setUserData(getNotificationUserData());
        sendNotification(notification);

    }

    private void sendStopFailNotification() {
        if (getName() == null)
            return;
        Notification notification = new Notification("error", getType() + "." + getName(), 1, "stop fail");
        notification.setTimeStamp(System.currentTimeMillis());
        sendNotification(notification);

    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }


    private synchronized boolean init() {
        ActorRepository.getInstance().register(this);
        while (state == INITING) {
            sendInProgressNotification();
            relax();
        }
        if (state == null || state == ActorState.INIT_FAILED) {
            try {
                state = INITING;
                initService();
                state = (INIT);
                createTime = new Date();
            } catch (Throwable t) {
                if (LOGGER.isErrorEnabled())
                    LOGGER.error("start error", t);
                state = INIT_FAILED;
                return false;
            }
        }
        return true;
    }

    /**
     * Start string.
     *
     * @param runasync the runasync
     * @return the string
     */
    protected final String start(boolean runasync) {
        try {
            if (!init()) {
                if (LOGGER.isWarnEnabled())
                    LOGGER.warn("actor {} is not init for start. status={}", toNameType(), getStateAsString());
                return getStateAsString();
            }
            while (state == STARTING || state == STOPPING) {
                sendInProgressNotification();
                if (LOGGER.isWarnEnabled())
                    LOGGER.warn("actor {} status is not suitable for start. relax and try again.status={}", toNameType(), getStateAsString());
                relax();
            }

            boolean validState = this.state == INIT || this.state == START_FAILED || this.state == STOPPED;
            if (!validState) {
                if (LOGGER.isWarnEnabled())
                    LOGGER.warn("actor {} status is not suitable for start. status={}", toNameType(), getStateAsString());
                return getStateAsString();
            }
            if (runasync) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("actor {} start async", toNameType());
                this.state = STARTED;
                lastStartedTime = new Date();
                sendStartNotification();
            } else
                this.state = (STARTING);
            // in async mode start service method lock until stop
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("starting actor {} async mode={}", toNameType(), runasync);
            startService();
            if (runasync) {
                sendStopNotification();
                this.state = (STOPPED);
            } else {
                this.state = (STARTED);
                lastStartedTime = new Date();
                sendStartNotification();
            }
        } catch (Throwable t) {
            state = (START_FAILED);

            if (LOGGER.isErrorEnabled())
                LOGGER.error("start error", t);
        }
        return getStateAsString();
    }

    public String start() {
        return start(false);
    }

    public String stop() {
        if (LOGGER.isWarnEnabled())
            LOGGER.warn("stopping actor that name={}", toNameType());

        while (state == STARTING || state == STOPPING) {
            sendInProgressNotification();
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("actor {} status is not suitable for stop. relax and try again.status={}", toNameType(), getStateAsString());
            relax();
        }
        if (state != STARTED) {
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("actor {} status is not started for stop. status={}", toNameType(), getStateAsString());
            return getStateAsString();
        }
        try {
            state = (STOPPING);
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("stopping actor {} ", toNameType());
            stopService();
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("stop actor {} ", toNameType());

            lastStoppedTime = new Date();
            state = (STOPPED);
            sendStopNotification();
        } catch (Throwable t) {
            state = (STOP_FAILED);
            if (LOGGER.isErrorEnabled())
                LOGGER.error("stop error", t);
            sendStopFailNotification();
        }
        return getStateAsString();
    }

    @Override
    public Date getLastStartedTime() {
        return lastStartedTime;
    }

    @Override
    public Date getLastStoppedTime() {
        return lastStoppedTime;
    }

    public ActorState getState() {
        return state;
    }

    public String getStateAsString() {
        if (state == null)
            return "null";
        return state.toString();
    }


    public boolean isRunning() {
        return state == STARTING || state == STARTED;
    }

    /**
     * Init service.
     *
     * @throws Throwable the throwable
     */
    protected void initService() throws Throwable {
    }

    @Override
    public void destroy() {
        if (LOGGER.isWarnEnabled())
            LOGGER.warn("start destroying actor={}", toNameType());
        try {
            stop();
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("error stop in destroy. maybe need restart system", e);
        }
        ActorRepository.getInstance().unregister(this);
        state = null;
        try {
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("destroying actor={}", toNameType());
            destroyService();
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("destroyed actor={}", toNameType());
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("error call destroy service in destroy. maybe need restart system", e);
        }


    }

    /**
     * Destroy service.
     */
    protected void destroyService() {

    }

    /**
     * Start service.
     *
     * @throws Throwable the throwable
     */
    protected abstract void startService() throws Throwable;

    /**
     * Stop service.
     *
     * @throws Throwable the throwable
     */
    protected abstract void stopService() throws Throwable;


    /**
     * Gets notification broadcaster support.
     *
     * @return the notification broadcaster support
     */
    public NotificationBroadcasterSupport getNotificationBroadcasterSupport() {
        return this;
    }

    @Override
    public void sendNotification(Notification notification) {
        super.sendNotification(notification);
    }


}
