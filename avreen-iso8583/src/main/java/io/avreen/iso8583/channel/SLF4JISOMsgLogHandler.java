package io.avreen.iso8583.channel;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.MessageDiagnosticKey;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.util.ISOMsgLogUtil;
import io.avreen.util.ChannelLogInfo;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * The class Slf 4 jiso msg log handler.
 */
@ChannelHandler.Sharable
public class SLF4JISOMsgLogHandler extends ChannelDuplexHandler implements Cloneable {
    private static final InternalLogger logger_in = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".iso8583.channel.SLF4JISOMsgLogHandler.in");
    private static final InternalLogger logger_out = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".iso8583.channel.SLF4JISOMsgLogHandler.out");
    private static final InternalLogger logger_in_reject = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".iso8583.channel.SLF4JISOMsgLogHandler.in.reject");
    private static final InternalLogger logger_out_reject = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".iso8583.channel.SLF4JISOMsgLogHandler.out.reject");
    private static Logger logger = LoggerFactory.getLogger(LoggerDomain.Name + ".iso8583.channel.SLF4JISOMsgLogHandler");

    private String name;
    private Set<Integer> excludeField = new HashSet<>();
    private Set<Integer> includeField = new HashSet<>();
    private Integer[] messageTraceField = null;
    private String messageTraceFieldDelimiter = ",";
    private boolean logHeader = true;


    /**
     * Instantiates a new Slf 4 jiso msg log handler.
     */
    public SLF4JISOMsgLogHandler() {
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

    /**
     * Gets exclude field.
     *
     * @return the exclude field
     */
    public Set<Integer> getExcludeField() {
        return excludeField;
    }

    /**
     * Sets exclude field.
     *
     * @param excludeField the exclude field
     */
    public void setExcludeField(Set<Integer> excludeField) {
        this.excludeField = excludeField;
    }

    /**
     * Gets include field.
     *
     * @return the include field
     */
    public Set<Integer> getIncludeField() {
        return includeField;
    }

    /**
     * Sets include field.
     *
     * @param includeField the include field
     */
    public void setIncludeField(Set<Integer> includeField) {
        this.includeField = includeField;
    }

    /**
     * Get message trace field integer [ ].
     *
     * @return the integer [ ]
     */
    public Integer[] getMessageTraceField() {
        return messageTraceField;
    }

    /**
     * Sets message trace field.
     *
     * @param messageTraceField the message trace field
     */
    public void setMessageTraceField(Integer[] messageTraceField) {
        this.messageTraceField = messageTraceField;
    }

    /**
     * Gets message trace field delimiter.
     *
     * @return the message trace field delimiter
     */
    public String getMessageTraceFieldDelimiter() {
        return messageTraceFieldDelimiter;
    }

    /**
     * Sets message trace field delimiter.
     *
     * @param messageTraceFieldDelimiter the message trace field delimiter
     */
    public void setMessageTraceFieldDelimiter(String messageTraceFieldDelimiter) {
        this.messageTraceFieldDelimiter = messageTraceFieldDelimiter;
    }

    /**
     * Is log header boolean.
     *
     * @return the boolean
     */
    public boolean isLogHeader() {
        return logHeader;
    }

    /**
     * Sets log header.
     *
     * @param logHeader the log header
     */
    public void setLogHeader(boolean logHeader) {
        this.logHeader = logHeader;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (logger_in.isInfoEnabled()) {
                if (getName() != null)
                    MessageDiagnosticKey.put("logger.name", getName());
                ISOMsgLogUtil msgLogUtil = new ISOMsgLogUtil(excludeField, includeField);
                msgLogUtil.setMessageTraceField(messageTraceField);
                msgLogUtil.setMessageTraceFieldDelimiter(messageTraceFieldDelimiter);
                msgLogUtil.setLogHeader(logHeader);
                ISOMsg isoMsg = (ISOMsg) msg;
                ChannelLogInfo channelLogInfo = null;
                if (ctx.channel() != null)
                    channelLogInfo = new ChannelLogInfo(ctx.channel().remoteAddress(), ctx.channel().localAddress(),ctx.channel().id().toString());
                String logEvent = msgLogUtil.buildIncomingISOMsgLog(getName(), isoMsg, channelLogInfo);
                if (isoMsg.isReject())
                    logger_in_reject.info(logEvent);
                else
                    logger_in.info(logEvent);

            }
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("log channel read event failed.", e);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        try {
            if (logger_out.isInfoEnabled()) {
                if (getName() != null)
                    MessageDiagnosticKey.put("logger.name", getName());
                ISOMsgLogUtil msgLogUtil = new ISOMsgLogUtil(excludeField, includeField);
                msgLogUtil.setMessageTraceField(messageTraceField);
                msgLogUtil.setMessageTraceFieldDelimiter(messageTraceFieldDelimiter);
                msgLogUtil.setLogHeader(logHeader);
                ISOMsg isoMsg = (ISOMsg) msg;
                ChannelLogInfo channelLogInfo = null;
                if (ctx.channel() != null)
                    channelLogInfo = new ChannelLogInfo(ctx.channel().remoteAddress(), ctx.channel().localAddress(),ctx.channel().id().toString());
                String logEvent = msgLogUtil.buildOutgoingISOMsgLog(getName(), isoMsg, channelLogInfo);
                if (isoMsg.isReject())
                    logger_out_reject.info(logEvent);
                else
                    logger_out.info(logEvent);
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("log channel write event failed.", e);
        }


    }
}