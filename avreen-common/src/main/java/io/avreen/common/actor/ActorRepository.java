package io.avreen.common.actor;


import io.avreen.common.jmx.MBeanExporter;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.SystemPropUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The class Actor repository.
 */
public class ActorRepository implements Runnable, ActorRepositoryMXBean {
    private static volatile ActorRepository instance = null;
    private static final ConcurrentHashMap<String, ActorBase> actorMap = new ConcurrentHashMap<String, ActorBase>();
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.actor.ActorRepository");
    private static String JmxDomain = SystemPropUtil.get("app.jmx.domain", "io.avreen");
    private boolean enableJMX = SystemPropUtil.getBoolean("app.actor.jmx.enable", true);

    private ActorRepository() {
        super();
    }

    private static void init(ActorRepository ins) {
        String objectName = JmxDomain + ":type=actor,name=" + "actor_container";
        MBeanExporter.register(ins, objectName);
        new Thread(ins).start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                ins.destroy();
            }
        }));
    }


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ActorRepository getInstance() {
        ActorRepository result = instance;
        if (result == null) {
            synchronized (ActorRepository.class) {
                result = instance;
                if (result == null) {
                    result = new ActorRepository();
                    init(result);
                    instance = result;
                }
            }
        }
        return result;
    }

    /**
     * Is enable jmx boolean.
     *
     * @return the boolean
     */
    public boolean isEnableJMX() {
        return enableJMX;
    }

    /**
     * Sets enable jmx.
     *
     * @param enableJMX the enable jmx
     */
    public void setEnableJMX(boolean enableJMX) {
        this.enableJMX = enableJMX;
    }


    private String buildJmxObjectName(ActorBase value) {

        return JmxDomain + ":type=actor,actor-type=" + value.getType() + ",name=" + value.getName();
    }

    /**
     * Register.
     *
     * @param value the value
     */
    public void register(ActorBase value) {
        if (value.getName() == null || value.getName().isEmpty())
            return;
        String key = value.getType() + "." + value.getName();
        if (actorMap.containsKey(key))
            return;
        actorMap.put(key, value);

        if (enableJMX) {
            if (value.isEnableJmx()) {
                String objectName = buildJmxObjectName(value);
                MBeanExporter.register(value, objectName);
            }
        }

    }

    /**
     * Unregister.
     *
     * @param value the value
     */
    public void unregister(ActorBase value) {
        if (value.getName() == null || value.getName().isEmpty())
            return;
        String key = value.getType() + "." + value.getName();
        actorMap.remove(key, value);
        if (enableJMX) {
            if (value.isEnableJmx()) {
                String objectName = buildJmxObjectName(value);
                MBeanExporter.unregister(objectName);
            }
        }
    }

    public void destroy() {
        for (String s : actorMap.keySet()) {
            actorMap.get(s).destroy();
        }
    }

    @Override
    public void shutdown(int waitSeconds) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                LOGGER.warn("!!!!!!!!!!!!! SHUTDOWN SYSTEM !!!!!!!!!!!!!");
                System.exit(0);
            }
        }, waitSeconds, waitSeconds, TimeUnit.SECONDS);

        destroy();
    }


    /**
     * Get actor base.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @param name the name
     * @return the actor base
     */
    public <T extends ActorBase > T get(String type, String name) {
        String key = type + "." + name;
        ActorBase obj = actorMap.get(key);
        return (T) obj;
    }

    /**
     * Get actor base.
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the actor base
     */
    public <T extends ActorBase > T get(String key) {
        ActorBase obj = actorMap.get(key);
        return (T) obj;
    }

    /**
     * Contains boolean.
     *
     * @param type the type
     * @param name the name
     * @return the boolean
     */
    public boolean contains(String type, String name) {
        String key = type + "." + name;
        return actorMap.containsKey(key);
    }

    /**
     * Gets actors.
     *
     * @return the actors
     */
    public ConcurrentHashMap.KeySetView<String, ActorBase> getActors() {
        return actorMap.keySet();
    }

    private void relax(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            /* nothing */
        }
    }


    @Override
    public void run() {

        while (true) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("check registered  actor's status");
            for (String key : actorMap.keySet()) {
                ActorBase actorBase = actorMap.get(key);
                int state = actorBase.getState();
                if (state == IActor.INIT_FAILED || state == IActor.START_FAILED) {
                    if (LOGGER.isWarnEnabled())
                        LOGGER.warn("restart actor name={}" + actorBase.getName());
                    try {
                        actorBase.start();
                    } catch (Exception e) {
                        if (LOGGER.isErrorEnabled())
                            LOGGER.error("restart failed  actor name={}" + actorBase.getName(), e);

                    }
                }
                relax(10);
            }
            relax(5000);
        }


    }
}
