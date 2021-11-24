package io.avreen.common.util;


import java.nio.charset.Charset;

/**
 * The class Charset setting.
 */
public class CharsetSetting {
    /**
     * The constant DEFAULT.
     */
    public static Charset DEFAULT = Charset.forName(SystemPropUtil.get("io.avreen.default-encoding", "Windows-1256"));
}
