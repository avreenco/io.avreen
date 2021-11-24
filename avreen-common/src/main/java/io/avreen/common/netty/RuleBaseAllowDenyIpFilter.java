package io.avreen.common.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * The class Rule base allow deny ip filter.
 */
@ChannelHandler.Sharable
public class RuleBaseAllowDenyIpFilter extends AbstractRemoteAddressFilter<InetSocketAddress> {
    private final IpFilterRule[] rules;
    /**
     * The White list.
     */
    ArrayList<IpFilterRule> whiteList = new ArrayList<>();
    /**
     * The Black list.
     */
    ArrayList<IpFilterRule> blackList = new ArrayList<>();

    /**
     * Instantiates a new Rule base allow deny ip filter.
     *
     * @param rules the rules
     */
    public RuleBaseAllowDenyIpFilter(IpFilterRule... rules) {
        if (rules == null) {
            throw new NullPointerException("rules");
        }

        this.rules = rules;
        for (IpFilterRule rule : rules) {
            if (rule.ruleType().equals(IpFilterRuleType.ACCEPT))
                whiteList.add(rule);
            else
                blackList.add(rule);
        }
    }

    @Override
    protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception {

        for (IpFilterRule rule : blackList) {
            if (rule.matches(remoteAddress))
                return false;
        }
        for (IpFilterRule rule : whiteList) {
            if (rule.matches(remoteAddress))
                return true;
        }
        return whiteList.size() == 0;
    }

    protected ChannelFuture channelRejected(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
        ctx.fireExceptionCaught(new IllegalStateException("cannot determine to accept or reject a channel: " + ctx.channel() + ". ip check failed"));
        return null;
    }

}
