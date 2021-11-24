package io.avreen.common.context;

import io.avreen.common.util.SystemPropUtil;

/**
 * The class Node info.
 */
public class NodeInfo {
    /**
     * The constant NODE_ID.
     */
    public static int NODE_ID = Integer.parseInt(SystemPropUtil.get("app.node-id", "1"));
}
