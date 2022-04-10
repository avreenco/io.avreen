package io.avreen.redis.common;

import io.avreen.common.context.NodeInfo;
import io.avreen.common.log.LoggerDomain;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulConnection;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The class Redis lock helper.
 */
public class RedisLockHelper {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".redis.common.RedisLockHelper");

    private StatefulConnection<String, byte[]> statefulConnection;
    private String sectionName;
    private int keepAliveSendPeriodSeconds = 7;
    private int extendKeepAliveSendPeriodMillis = 2000;

    /**
     * Instantiates a new Redis lock helper.
     *
     * @param statefulConnection the stateful connection
     * @param sectionName        the section name
     */
    public RedisLockHelper(StatefulConnection<String, byte[]> statefulConnection, String sectionName) {
        this.statefulConnection = statefulConnection;
        this.sectionName = sectionName;
    }


    private String getActivePassiveControlKey(String sectionName) {
        return "active-passive-key." + sectionName;
    }

    private byte[] getActivePassiveControlValue(String sectionName) {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        String nodeInfo = "Node[" +  Integer.valueOf(NodeInfo.NODE_ID).toString() + "]" + " JVM[" + jvmName + "]";
        return nodeInfo.getBytes();
    }

    /**
     * Lock.
     */
    public void lock() {
        while (true) {
            try {
                byte[] active_node_info = getActivePassiveControlValue(sectionName);
                String val = RedisCommandsUtil.getStringCommands(statefulConnection).set(getActivePassiveControlKey(sectionName), active_node_info, new SetArgs().ex(keepAliveSendPeriodSeconds).nx());
                boolean isSet = (val != null);
                if (isSet) {
                    logger.warn("active current node-id={} sectionName={} active-node-info={}", NodeInfo.NODE_ID, sectionName, new String(getActivePassiveControlValue(sectionName)));
                    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            logger.warn("set keep alive for current node-id={} sectionName={} active-node-info={}", NodeInfo.NODE_ID, sectionName, new String(getActivePassiveControlValue(sectionName)));
                            RedisCommandsUtil.getStringCommands(statefulConnection).set(getActivePassiveControlKey(sectionName), active_node_info, new SetArgs().ex(keepAliveSendPeriodSeconds));
                        }
                    }, 0, extendKeepAliveSendPeriodMillis, TimeUnit.MILLISECONDS);
                    break;
                }
                logger.warn("passive current node for  node-id={} sectionName={} active-node-info={}", NodeInfo.NODE_ID, sectionName, new String(RedisCommandsUtil.getStringCommands(statefulConnection).get(getActivePassiveControlKey(sectionName))));
                Thread.sleep(extendKeepAliveSendPeriodMillis);
            } catch (Exception e) {
                logger.error("exception in check active node", e);
            }
        }

    }

    /**
     * Gets keep alive send period seconds.
     *
     * @return the keep alive send period seconds
     */
    public int getKeepAliveSendPeriodSeconds() {
        return keepAliveSendPeriodSeconds;
    }

    /**
     * Sets keep alive send period seconds.
     *
     * @param keepAliveSendPeriodSeconds the keep alive send period seconds
     * @return the keep alive send period seconds
     */
    public RedisLockHelper setKeepAliveSendPeriodSeconds(int keepAliveSendPeriodSeconds) {
        this.keepAliveSendPeriodSeconds = keepAliveSendPeriodSeconds;
        return this;
    }

    /**
     * Gets extend keep alive send period millis.
     *
     * @return the extend keep alive send period millis
     */
    public int getExtendKeepAliveSendPeriodMillis() {
        return extendKeepAliveSendPeriodMillis;
    }

    /**
     * Sets extend keep alive send period millis.
     *
     * @param extendKeepAliveSendPeriodMillis the extend keep alive send period millis
     * @return the extend keep alive send period millis
     */
    public RedisLockHelper setExtendKeepAliveSendPeriodMillis(int extendKeepAliveSendPeriodMillis) {
        this.extendKeepAliveSendPeriodMillis = extendKeepAliveSendPeriodMillis;
        return this;
    }
}
