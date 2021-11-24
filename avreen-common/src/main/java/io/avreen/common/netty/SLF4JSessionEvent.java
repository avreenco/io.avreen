package io.avreen.common.netty;

import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.MessageDiagnosticKey;
import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.SocketAddress;
import java.util.HashMap;

/**
 * The class Slf 4 j session event.
 */
public class SLF4JSessionEvent implements ISessionEventListener {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.SLF4JSessionEvent");
    private static InternalLogger logger_error = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.ErrorLogger");
    private String name;

    private static HashMap<ChannelEventTypes, InternalLogLevel> eventLogLevel = new HashMap<>();

    static {
        eventLogLevel.put(ChannelEventTypes.channelInactive, InternalLogLevel.WARN);
        eventLogLevel.put(ChannelEventTypes.channelUnregistered, InternalLogLevel.WARN);
        eventLogLevel.put(ChannelEventTypes.exceptionCaught, InternalLogLevel.ERROR);
        eventLogLevel.put(ChannelEventTypes.disconnect, InternalLogLevel.WARN);
        eventLogLevel.put(ChannelEventTypes.close, InternalLogLevel.WARN);
    }

    /**
     * Instantiates a new Slf 4 j session event.
     *
     * @param name the name
     */
    public SLF4JSessionEvent(String name) {
        this.name = name;
    }

    /**
     * Instantiates a new Slf 4 j session event.
     */
    public SLF4JSessionEvent() {
        name = "no_name_channel";
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void fire(ChannelEventTypes eventName, Channel channel, Throwable e) {
        if (getName() != null)
            MessageDiagnosticKey.put("session.name", getName());
        InternalLogLevel logLevel = InternalLogLevel.INFO;
        if (eventLogLevel.containsKey(eventName))
            logLevel = eventLogLevel.get(eventName);
        if (logger.isEnabled(logLevel)) {

            SessionLogUtil sessionLogUtil = new SessionLogUtil();
            String log = sessionLogUtil.buildSessionEventLog(name, eventName.toString(), channel, e);
            logger.log(logLevel, log);
        }
        if (e != null)
            logger_error.error(e.getMessage(), e);

    }

    @Override
    public void fire(ChannelEventTypes eventName, SocketAddress remoteAddress, SocketAddress localAddress, Throwable e) {
        if (getName() != null)
            MessageDiagnosticKey.put("session.name", getName());
        InternalLogLevel logLevel = InternalLogLevel.INFO;
        if (eventLogLevel.containsKey(eventName))
            logLevel = eventLogLevel.get(eventName);
        if (logger.isEnabled(logLevel)) {
            SessionLogUtil sessionLogUtil = new SessionLogUtil();
            String log = sessionLogUtil.buildSessionEventLog(name, eventName.toString(), remoteAddress, localAddress);
            logger.log(logLevel, log);
        }

    }

}
