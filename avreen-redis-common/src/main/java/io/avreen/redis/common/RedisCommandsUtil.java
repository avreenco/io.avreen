package io.avreen.redis.common;

import io.avreen.common.log.LoggerDomain;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisListAsyncCommands;
import io.lettuce.core.api.async.RedisSortedSetAsyncCommands;
import io.lettuce.core.api.sync.*;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.masterreplica.MasterReplica;
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.time.Duration;
import java.util.ArrayList;

/**
 * The class Redis commands util.
 */
public class RedisCommandsUtil {

    private static ClientResources clientResources;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.impl.redis.RedisSubscriber");

    static {


        clientResources = DefaultClientResources.create();

    }

    /**
     * Gets list commands.
     *
     * @param statefulConnection the stateful connection
     * @return the list commands
     */
    public static RedisListCommands<String, byte[]> getListCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).sync();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).sync();
        throw new RuntimeException("invalid redis connection class");
    }

    public static RedisSortedSetCommands<String, byte[]> getSortedSetCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).sync();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).sync();
        throw new RuntimeException("invalid redis connection class");
    }

    /**
     * Gets scripting commands.
     *
     * @param statefulConnection the stateful connection
     * @return the scripting commands
     */
    public static RedisScriptingCommands<String, byte[]> getScriptingCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).sync();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).sync();
        throw new RuntimeException("invalid redis connection class");
    }


    /**
     * Gets async list commands.
     *
     * @param statefulConnection the stateful connection
     * @return the async list commands
     */
    public static RedisListAsyncCommands<String, byte[]> getAsyncListCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).async();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).async();
        throw new RuntimeException("invalid redis connection class");
    }

    public static RedisSortedSetAsyncCommands<String, byte[]> getAsyncSortedSetCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).async();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).async();
        throw new RuntimeException("invalid redis connection class");
    }

    /**
     * Gets string commands.
     *
     * @param statefulConnection the stateful connection
     * @return the string commands
     */
    public static RedisStringCommands<String, byte[]> getStringCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).sync();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).sync();
        throw new RuntimeException("invalid redis connection class");
    }

    public static RedisHashCommands<String, byte[]> getHashCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).sync();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).sync();
        throw new RuntimeException("invalid redis connection class");
    }

    /**
     * Gets key commands.
     *
     * @param statefulConnection the stateful connection
     * @return the key commands
     */
    public static RedisKeyCommands<String, byte[]> getKeyCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).sync();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).sync();
        throw new RuntimeException("invalid redis connection class");
    }

    /**
     * Gets server commands.
     *
     * @param <T>                the type parameter
     * @param statefulConnection the stateful connection
     * @return the server commands
     */
    public static <T> RedisServerCommands<String, byte[]> getServerCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).sync();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).sync();
        throw new RuntimeException("invalid redis connection class");
    }

    /**
     * Gets base commands.
     *
     * @param <T>                the type parameter
     * @param statefulConnection the stateful connection
     * @return the base commands
     */
    public static <T> BaseRedisCommands<String, byte[]> getBaseCommands(StatefulConnection<String, byte[]> statefulConnection) {
        if (statefulConnection instanceof StatefulRedisConnection)
            return ((StatefulRedisConnection<String, byte[]>) statefulConnection).sync();
        else if (statefulConnection instanceof StatefulRedisClusterConnection)
            return ((StatefulRedisClusterConnection<String, byte[]>) statefulConnection).sync();
        throw new RuntimeException("invalid redis connection class");
    }


    /**
     * Connect stateful connection.
     *
     * @param redisClientConfig the redis client config
     * @return the stateful connection
     */
    public static  StatefulConnection<String, byte[]> connect(RedisClientConfigProperties redisClientConfig) {
        if (!redisClientConfig.isClusterMode()) {
            RedisClient redisClient = buildRedisClient(redisClientConfig);
//            StatefulRedisConnection<String, byte[]> connect = redisClient.connect(StringByteArrayCodec.INSTANCE);
            RedisURI redisURI = RedisURI.create(redisClientConfig.getRedisUri());
            StatefulRedisMasterReplicaConnection<String, byte[]> connect = MasterReplica.connect(redisClient, StringByteArrayCodec.INSTANCE, redisURI);

            connect.setReadFrom(ReadFrom.UPSTREAM);
            return connect;
        } else {
            RedisClusterClient redisClient = buildClusterRedisClient(redisClientConfig);
            StatefulRedisClusterConnection<String, byte[]> connect = redisClient.connect(StringByteArrayCodec.INSTANCE);
            return connect;
        }
    }

    /**
     * Connect pub sub stateful redis pub sub connection.
     *
     * @param redisClientConfig the redis client config
     * @return the stateful redis pub sub connection
     */
    public static StatefulRedisPubSubConnection<String, byte[]> connectPubSub(RedisClientConfigProperties redisClientConfig) {
        if (!redisClientConfig.isClusterMode()) {
            RedisClient redisClient = buildRedisClient(redisClientConfig);
            StatefulRedisPubSubConnection<String, byte[]> connect = redisClient.connectPubSub(StringByteArrayCodec.INSTANCE);
//            RedisURI redisURI = RedisURI.create(redisClientConfig.getRedisUri());
//            StatefulRedisMasterReplicaConnection<String, byte[]> connect = MasterReplica.connect(redisClient, StringByteArrayCodec.INSTANCE, redisURI);
//            connect.setReadFrom(ReadFrom.MASTER_PREFERRED);
            return connect;
        } else {
            RedisClusterClient redisClient = buildClusterRedisClient(redisClientConfig);
            StatefulRedisPubSubConnection<String, byte[]> connect = redisClient.connectPubSub(StringByteArrayCodec.INSTANCE);
            return connect;
        }
    }

    private static void buildClientOption(RedisClientConfigProperties redisClientConfig, ClientOptions.Builder clientOptionsBuilder) {
        SocketOptions.Builder socketOptionsBuilder = SocketOptions.builder();
        if (redisClientConfig.getConnectTimeout() != null)
            socketOptionsBuilder.connectTimeout(redisClientConfig.getConnectTimeout());
        if (redisClientConfig.getTcpNoDelay() != null)
            socketOptionsBuilder.tcpNoDelay(redisClientConfig.getTcpNoDelay());
        if (redisClientConfig.getKeepAlive() != null)
            socketOptionsBuilder.keepAlive(redisClientConfig.getKeepAlive());

        clientOptionsBuilder.socketOptions(socketOptionsBuilder.build());
        if (redisClientConfig.getAutoReconnect() != null)
            clientOptionsBuilder.autoReconnect(redisClientConfig.getAutoReconnect());
        clientOptionsBuilder.protocolVersion(redisClientConfig.getProtocolVersion());
    }


    private static RedisClient buildRedisClient(RedisClientConfigProperties redisClientConfig) {
        RedisURI redisURI = RedisURI.create(redisClientConfig.getRedisUri());
        RedisClient redisClient = RedisClient.create(clientResources, redisURI);
        ClientOptions.Builder clientOptionsBuilder = ClientOptions.builder();
        buildClientOption(redisClientConfig, clientOptionsBuilder);
        ClientOptions clientOptions = clientOptionsBuilder.build();
        redisClient.setOptions(clientOptions);
        return redisClient;
    }


    private static RedisClusterClient buildClusterRedisClient(RedisClientConfigProperties redisClientConfig) {

        ArrayList<RedisURI> redisURIS = new ArrayList<>();
        for (String s : redisClientConfig.getRedisClusterUriList()) {
            RedisURI redisURI = RedisURI.create(s);
            redisURIS.add(redisURI);

        }

        ClusterClientOptions.Builder clientOptionsBuilder = ClusterClientOptions.builder();
        buildClientOption(redisClientConfig, clientOptionsBuilder);

        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder().enablePeriodicRefresh(Duration.ofSeconds(10 * 60))
                .enableAllAdaptiveRefreshTriggers().build();
        RedisClusterClient redisClient = RedisClusterClient.create(clientResources, redisURIS);
        ClusterClientOptions clientOptions = clientOptionsBuilder.topologyRefreshOptions(clusterTopologyRefreshOptions).build();
        redisClient.setOptions(clientOptions);
        return redisClient;
    }


}
