package io.avreen.redis.common;

import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulConnection;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The class Redis metric monitor.
 */
public class RedisMetricMonitor {

    //private static final String __MON_CLIENT_NAME = "jmx-agent-";
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private StatefulConnection<String, byte[]> statefulConnection;
    private RedisClientConfigProperties redisClientConfig;
    private String callerName;

    /**
     * Instantiates a new Redis metric monitor.
     *
     * @param callerName        the caller name
     * @param redisClientConfig the redis client config
     */
    public RedisMetricMonitor(String callerName, RedisClientConfigProperties redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
        this.callerName = callerName;
    }

    /**
     * Gets last message.
     *
     * @param queue the queue
     * @return the last message
     */
    public List<String> getLastMessage(String queue) {
        checkConnection();
        return RedisUtil.getLastMessages(RedisCommandsUtil.getListCommands(statefulConnection), queue, 0L, 0L);
    }

    /**
     * Gets last messages.
     *
     * @param queue the queue
     * @param start the start
     * @param end   the end
     * @return the last messages
     */
    public List<String> getLastMessages(String queue, long start, long end) {
        checkConnection();
        return RedisUtil.getLastMessages(RedisCommandsUtil.getListCommands(statefulConnection), queue, 0L, 0L);
    }

    /**
     * Ping string.
     *
     * @return the string
     */
    public String ping() {
        checkConnection();
        return RedisUtil.ping(RedisCommandsUtil.getBaseCommands(statefulConnection));
    }

    /**
     * Gets client info.
     *
     * @param clientName the client name
     * @return the client info
     */
    public List<Map<String, String>> getClientInfo(String clientName) {
        checkConnection();
        return RedisUtil.getClientInfo(RedisCommandsUtil.getServerCommands(statefulConnection), clientName);
    }

    /**
     * Gets client id.
     *
     * @param clientName the client name
     * @return the client id
     */
    public long getClientID(String clientName) {
        checkConnection();
        return RedisUtil.getClientID(RedisCommandsUtil.getServerCommands(statefulConnection), clientName);
    }


    /**
     * Gets pending count.
     *
     * @param queue the queue
     * @return the pending count
     */
    public Long getPendingCount(String queue) {
        checkConnection();
        return RedisUtil.getPendingCount(RedisCommandsUtil.getListCommands(statefulConnection), queue);
    }

    /**
     * Rpush long.
     *
     * @param queue the queue
     * @param val   the val
     * @return the long
     */
    public Long rpush(String queue, byte[] val) {
        checkConnection();
        return RedisUtil.rpush(RedisCommandsUtil.getListCommands(statefulConnection), queue, val);
    }

    public Long zadd(String queue, byte[] val) {
        checkConnection();
        return RedisCommandsUtil.getSortedSetCommands(statefulConnection).zadd(queue, 1, val);
    }

    /**
     * Sets .
     *
     * @param key the key
     * @param val the val
     * @return the
     */
    public Boolean setnx(String key, byte[] val) {
        checkConnection();
        boolean result = RedisCommandsUtil.getStringCommands(statefulConnection).setnx(key, val);
        return result;
    }

    /**
     * Set string.
     *
     * @param key     the key
     * @param val     the val
     * @param setArgs the set args
     * @return the string
     */
    public String set(String key, byte[] val, SetArgs setArgs) {
        checkConnection();
        return RedisCommandsUtil.getStringCommands(statefulConnection).set(key, val, setArgs);
    }

    /**
     * Wait for replication long.
     *
     * @param replicateNumber the replicate number
     * @param timeout         the timeout
     * @return the long
     */
    public Long waitForReplication(int replicateNumber, long timeout) {
        checkConnection();
        return RedisCommandsUtil.getBaseCommands(statefulConnection).waitForReplication(replicateNumber, timeout);
    }

    /**
     * Config get map.
     *
     * @param cfgID the cfg id
     * @return the map
     */
    public Map<String, String> configGet(String cfgID) {
        checkConnection();
        return RedisUtil.configGet(RedisCommandsUtil.getServerCommands(statefulConnection), cfgID);
    }

    /**
     * Execute command string.
     *
     * @param fullCommand the full command
     * @return the string
     */
    public String executeCommand(String fullCommand) {

        checkConnection();
        return RedisUtil.executeCommand(RedisCommandsUtil.getBaseCommands(statefulConnection), fullCommand);
    }

    /**
     * Get byte [ ].
     *
     * @param key the key
     * @return the byte [ ]
     */
    public byte[] get(String key) {
        checkConnection();
        return RedisCommandsUtil.getStringCommands(statefulConnection).get(key);
    }

    /**
     * Check connection stateful connection.
     *
     * @return the stateful connection
     */
    public StatefulConnection<String, byte[]> checkConnection() {
        if (statefulConnection != null)
            return statefulConnection;
        synchronized (this) {
            if (statefulConnection != null)
                return statefulConnection;
            StatefulConnection<String, byte[]> session = RedisCommandsUtil.connect(redisClientConfig);
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            String sessionName = "monitoring-" + callerName + "-" + jvmName + "-" + atomicInteger.incrementAndGet();
            RedisUtil.setSessionInfo(session, sessionName);
            statefulConnection = session;
        }
        return statefulConnection;
    }

}
