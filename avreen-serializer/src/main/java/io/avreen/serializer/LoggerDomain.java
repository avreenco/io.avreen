package io.avreen.serializer;

import io.netty.util.internal.SystemPropertyUtil;

/**
 * The class Logger domain.
 */
class LoggerDomain
{
    /**
     * The constant Name.
     */
    public static String Name = SystemPropertyUtil.get("core.log.domain", "io.avreen");
}
