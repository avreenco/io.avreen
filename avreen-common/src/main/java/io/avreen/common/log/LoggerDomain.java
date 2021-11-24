package io.avreen.common.log;


import io.avreen.common.util.SystemPropUtil;

/**
 * The class Logger domain.
 */
public class LoggerDomain {
    /**
     * The constant Name.
     */
    public static String Name = SystemPropUtil.get("core.log.domain", "io.avreen");
}
