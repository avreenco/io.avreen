package io.avreen.redis.common;

import io.avreen.common.util.CodecUtil;
import io.lettuce.core.StatefulRedisConnectionImpl;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.sync.BaseRedisCommands;
import io.lettuce.core.api.sync.RedisListCommands;
import io.lettuce.core.api.sync.RedisServerCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.IntegerOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.ProtocolKeyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The class Redis util.
 */
public class RedisUtil {

    /**
     * To string connection status string.
     *
     * @param statefulRedisConnection the stateful redis connection
     * @return the string
     */
    static String toStringConnectionStatus(StatefulConnection statefulRedisConnection) {
        if (statefulRedisConnection == null)
            return "NotInit";
        if (statefulRedisConnection.isOpen())
            return "Open";
        return "Close";

    }

    /**
     * Sets session info.
     *
     * @param statefulRedisConnection the stateful redis connection
     * @param sessionName             the session name
     */
    public static void setSessionInfo(StatefulConnection statefulRedisConnection, String sessionName) {
        if (statefulRedisConnection instanceof StatefulRedisConnectionImpl)
            ((StatefulRedisConnectionImpl) statefulRedisConnection).setClientName(sessionName);
    }


    /**
     * Gets client list.
     *
     * @param redisCommands the redis commands
     * @return the client list
     */
    public static String getClientList(RedisServerCommands redisCommands) {
        return redisCommands.clientList();
    }


    /**
     * Gets pending count.
     *
     * @param redisCommands the redis commands
     * @param queue         the queue
     * @return the pending count
     */
    public static Long getPendingCount(RedisListCommands redisCommands, String queue) {
        return redisCommands.llen(queue);
    }


    /**
     * Gets last messages.
     *
     * @param redisCommands the redis commands
     * @param queue         the queue
     * @param start         the start
     * @param end           the end
     * @return the last messages
     */
    static List<String> getLastMessages(RedisListCommands redisCommands, String queue, long start, long end) {
        List<byte[]> bytes = redisCommands.lrange(queue, start, end);
        List<String> stringList = new ArrayList<>();
        bytes.forEach(bytes1 -> stringList.add("(" + bytes1.length + ")" + CodecUtil.hexString(bytes1)));
        return stringList;
    }


    /**
     * Ping string.
     *
     * @param redisCommands the redis commands
     * @return the string
     */
    static String ping(BaseRedisCommands redisCommands) {
        return redisCommands.ping();
    }

    /**
     * Gets client info.
     *
     * @param redisCommands the redis commands
     * @param clientName    the client name
     * @return the client info
     */
    static List<Map<String, String>> getClientInfo(RedisServerCommands redisCommands, String clientName) {
        return expandClientList(redisCommands.clientList(), clientName);
    }

    /**
     * Gets client id.
     *
     * @param redisCommands the redis commands
     * @param clientName    the client name
     * @return the client id
     */
    static long getClientID(RedisServerCommands redisCommands, String clientName) {
        List<Map<String, String>> maps = expandClientList(redisCommands.clientList(), clientName);
        if (maps == null || maps.size() == 0)
            return 0L;
        Map<String, String> clinetItems = maps.get(0);
        if (clinetItems.containsKey("id"))
            return Long.parseLong(clinetItems.get("id"));
        return 0L;
    }

    /**
     * Gets client id.
     *
     * @param redisCommands the redis commands
     * @return the client id
     */
    static long getClientID(BaseRedisCommands<String, byte[]> redisCommands) {
        ProtocolKeyword keyword = CommandType.CLIENT;
        IntegerOutput<String, byte[]> commandOutput = new IntegerOutput(StringByteArrayCodec.INSTANCE);
        CommandArgs commandArgs = new CommandArgs(StringByteArrayCodec.INSTANCE);
        commandArgs.addValues("ID".getBytes());
        return redisCommands.dispatch(keyword, commandOutput, commandArgs);
    }

    /**
     * Execute command string.
     *
     * @param redisCommands the redis commands
     * @param fullCommand   the full command
     * @return the string
     */
    static String executeCommand(BaseRedisCommands redisCommands, String fullCommand) {
        CLICommandOutput<String, String> commandOutput = new CLICommandOutput(StringCodec.ASCII);
        String[] split = fullCommand.split(" ");
        String name = split[0].trim();
        String[] params = null;
        if (split.length > 1) {
            params = new String[split.length - 1];
            for (int idx = 1; idx < split.length; idx++)
                params[idx - 1] = split[idx].trim();
        }

        CommandArgs commandArgs = new CommandArgs(StringCodec.ASCII);
        if (params != null)
            commandArgs.addValues(params);
        try {
            redisCommands.dispatch(new ProtocolKeyword() {
                @Override
                public byte[] getBytes() {
                    return name.getBytes();
                }

                @Override
                public String name() {
                    return name;
                }
            }, commandOutput, commandArgs);
            List<Object> objects = commandOutput.get();
            if (objects == null)
                return null;
            StringBuilder stringBuilder = new StringBuilder();
            objects.forEach(new Consumer<Object>() {
                @Override
                public void accept(Object o) {
                    stringBuilder.append(o);
                    stringBuilder.append(System.lineSeparator());
                }
            });

            return stringBuilder.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    /**
     * Config get map.
     *
     * @param redisCommands the redis commands
     * @param cfgID         the cfg id
     * @return the map
     */
    static Map<String, String> configGet(RedisServerCommands redisCommands, String cfgID) {
        return redisCommands.configGet(cfgID);
    }

    private static List<Map<String, String>> expandClientList(String clientListResult, String clientName) {
        String[] allClients = clientListResult.split("\n");
        List<Map<String, String>> clientList = new ArrayList<>();
        for (String client : allClients) {
            String[] allFields = client.split(" ");
            Map<String, String> stringMap = new HashMap<>();
            String itemClientName = null;
            for (String f : allFields) {
                String[] item = f.split("=");
                String key = item[0];
                String val = null;
                if (item.length > 1)
                    val = item[1];
                if (val != null) {
                    stringMap.put(key, val);
                    if ("name".equals(key)) {
                        itemClientName = val;
                    }
                }
            }
            if (clientName != null && !clientName.isEmpty()) {
                if (clientName.equals(itemClientName)) {
                    clientList.add(stringMap);
                }
            } else
                clientList.add(stringMap);
        }
        return clientList;
    }
    public static Map<String, String> expandInfoResult(String infoResult) {
        String[] infoRow = infoResult.split("\r\n");
        Map<String, String> infoMap = new HashMap<>();
        for (String client : infoRow) {
            String[] allFields = client.split(":");
            if(allFields.length==2)
                infoMap.put(allFields[0],allFields[1]);
        }
        return infoMap;
    }
    public static int compareVersions(String version1, String version2) {
        int comparisonResult = 0;

        String[] version1Splits = version1.split("\\.");
        String[] version2Splits = version2.split("\\.");
        int maxLengthOfVersionSplits = Math.max(version1Splits.length, version2Splits.length);

        for (int i = 0; i < maxLengthOfVersionSplits; i++){
            Integer v1 = i < version1Splits.length ? Integer.parseInt(version1Splits[i]) : 0;
            Integer v2 = i < version2Splits.length ? Integer.parseInt(version2Splits[i]) : 0;
            int compare = v1.compareTo(v2);
            if (compare != 0) {
                comparisonResult = compare;
                break;
            }
        }
        return comparisonResult;
    }
    /**
     * Rpush long.
     *
     * @param redisCommands the redis commands
     * @param queue         the queue
     * @param val           the val
     * @return the long
     */
    public static Long rpush(RedisListCommands<String, byte[]> redisCommands, String queue, byte[] val) {
        return redisCommands.rpush(queue, val);
    }

    public static String getVersion(StatefulConnection connect) {
        String server_info = RedisCommandsUtil.getServerCommands(connect).info("Server");
        Map<String, String> stringStringMap = RedisUtil.expandInfoResult(server_info);
        return stringStringMap.get("redis_version");
    }


//    static String time(StatefulRedisConnection statefulRedisConnection) {
//        RedisCommands<String, byte[]> sync = statefulRedisConnection.sync();
//        return sync.time();
//    }

}
