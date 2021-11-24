package io.avreen.common.netty;

import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;

import java.util.List;

/**
 * The class Ip filter util.
 */
public class IPFilterUtil {
    /**
     * Add source.
     *
     * @param sources        the sources
     * @param values         the values
     * @param filterRuleType the filter rule type
     */
    public static void addSource(List<IpSubnetFilterRule> sources, List<String> values, IpFilterRuleType filterRuleType) {
        for (String arg : values) {
            addSource(sources, arg, filterRuleType);
        }
    }

    /**
     * Add source.
     *
     * @param sources        the sources
     * @param source         the source
     * @param filterRuleType the filter rule type
     */
    public static void addSource(List<IpSubnetFilterRule> sources, String source, IpFilterRuleType filterRuleType) {
        if (!source.contains("/")) { // no netmask, add default
            source = source + "/32";
        }

        String[] ipAddressCidrPrefix = source.split("/", 2);
        String ipAddress = ipAddressCidrPrefix[0];
        int cidrPrefix = Integer.parseInt(ipAddressCidrPrefix[1]);

        sources.add(
                new IpSubnetFilterRule(ipAddress, cidrPrefix, filterRuleType));
    }


}
