package io.avreen.common.context;

import io.avreen.common.util.SystemPropUtil;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

/**
 * The class Context key util.
 */
public class ContextKeyUtil {
    /**
     * The constant MSG_TRACE.
     */
    public static String MSG_TRACE = SystemPropUtil.get("io.avreen.mdc.msgTrace", "__MsgTrace__");
    private static SequenceGenerator sequenceGenerator = new SequenceGenerator();


    private static Base64.Encoder encoder = Base64.getEncoder().withoutPadding();

    private static byte[] getBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    private static String getBase64String(UUID uuid) {
        return encoder.encodeToString(getBytes(uuid));
    }

    /**
     * Gen random string string.
     *
     * @return the string
     */
    public static String genRandomString() {

        return Long.toString(sequenceGenerator.nextId());
        //return getBase64String(UUID.randomUUID());
    }

}
