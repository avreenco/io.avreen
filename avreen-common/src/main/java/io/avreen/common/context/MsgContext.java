package io.avreen.common.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class Msg context.
 *
 * @param <M> the type parameter
 */
public class MsgContext<M> implements Serializable {
    /**
     * The constant NULL.
     */
    public static MsgContext NULL = new MsgContext();
    private HashMap<String, Object> hashMap = new HashMap<String, Object>();
    private HashMap<String, Object> processContext = null;
    private List<Integer> passingNodes = new ArrayList<>();
    private String creator;
    private M msg;
    private long captureTime = 0;
    private long expireTime = 0;
    private String callbackQueue;
    private String callbackPublisher;
    private String channelId;
    private MsgTracer tracer;
    private long elapse = 0;
    private Integer edgeNodeId;


    /**
     * Instantiates a new Msg context.
     *
     * @param creator   the creator
     * @param msg       the msg
     * @param msgTracer the msg tracer
     */
    public MsgContext(String creator, M msg, MsgTracer msgTracer) {
        this.msg = msg;
        this.creator = creator;
        if (msgTracer == null) {
            msgTracer = MsgTracer.current();
        }
        this.tracer = msgTracer;
        addPassingNode(NodeInfo.NODE_ID);
    }

    private Map<String, Object> getProcessContext() {
        if (processContext == null)
            processContext = new HashMap<>();
        return processContext;
    }

    /**
     * Instantiates a new Msg context.
     *
     * @param creator the creator
     * @param msg     the msg
     */
    public MsgContext(String creator, M msg) {
        this(creator, msg, null);
    }


    /**
     * Instantiates a new Msg context.
     *
     * @param msg    the msg
     * @param parent the parent
     */
    public MsgContext(M msg, MsgContext parent) {
        this(msg, parent, true);
    }

    /**
     * Instantiates a new Msg context.
     *
     * @param msg         the msg
     * @param parent      the parent
     * @param copyContext the copy context
     */
    public MsgContext(M msg, MsgContext parent, boolean copyContext) {
        this.msg = msg;
        this.creator = parent.creator;
        if (copyContext) {
            this.hashMap = parent.hashMap;
            this.processContext = parent.processContext;
        }
        this.tracer = parent.tracer;
        this.elapse = parent.elapse;
        this.channelId = parent.channelId;
        this.captureTime = parent.captureTime;
        this.edgeNodeId = parent.edgeNodeId;
        this.callbackPublisher = parent.callbackPublisher;
        this.callbackQueue = parent.callbackQueue;
        this.passingNodes = parent.passingNodes;
        addPassingNode(NodeInfo.NODE_ID);
    }

    private MsgContext() {
        this.msg = null;
        this.creator = null;
    }

    /**
     * Gets callback publisher.
     *
     * @return the callback publisher
     */
    public String getCallbackPublisher() {
        return callbackPublisher;
    }


    /**
     * Clear process context.
     */
    public void clearProcessContext() {
        if (processContext != null)
            processContext.clear();
    }

    /**
     * Gets channel id.
     *
     * @return the channel id
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Sets channel id.
     *
     * @param channelId the channel id
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * Gets creator.
     *
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Gets callback queue.
     *
     * @return the callback queue
     */
    public String getCallbackQueue() {
        return callbackQueue;
    }

    /**
     * Sets callback queue.
     *
     * @param callbackPublisher the callback publisher
     * @param callbackQueue     the callback queue
     */
    public void setCallbackQueue(String callbackPublisher, String callbackQueue) {
        this.callbackPublisher = callbackPublisher;
        this.callbackQueue = callbackQueue;
    }


    /**
     * Gets capture time.
     *
     * @return the capture time
     */
    public long getCaptureTime() {
        return captureTime;
    }

    /**
     * Sets capture time.
     *
     * @param captureTime the capture time
     */
    public void setCaptureTime(long captureTime) {
        this.captureTime = captureTime;
    }

    /**
     * Gets expire time.
     *
     * @return the expire time
     */
    public long getExpireTime() {
        return expireTime;
    }

    /**
     * Sets expire time.
     *
     * @param expireTime the expire time
     */
    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public M getMsg() {
        return msg;
    }

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public Object get(String key) {
        return hashMap.get(key);
    }

    /**
     * Gets from process context.
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the from process context
     */
    public <T> T getFromProcessContext(String key) {
        return (T) getProcessContext().get(key);
    }

    /**
     * Gets from process context.
     *
     * @param <T> the type parameter
     * @param clz the clz
     * @return the from process context
     */
    public <T> T getFromProcessContext(Class<T>  clz)
    {
        for (Object value : getProcessContext().values()) {
            if(clz != null && value!=null && clz.isAssignableFrom(value.getClass()))
                return (T) value;
        }
        return null;
    }


    /**
     * Take object.
     *
     * @param key the key
     * @return the object
     */
    public Object take(String key) {
        return hashMap.remove(key);
    }

    /**
     * Take from process context object.
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the object
     */
    public <T> T takeFromProcessContext(String key) {
        return (T) getProcessContext().remove(key);
    }


    /**
     * Put.
     *
     * @param key the key
     * @param val the val
     */
    public void put(String key, Object val) {
        hashMap.put(key, val);
    }

    /**
     * Put into process context.
     *
     * @param key the key
     * @param val the val
     */
    public void putIntoProcessContext(String key, Object val) {
        getProcessContext().put(key, val);
    }

    public void clearFromProcessContextByType(Class<?> clazz) {
        getProcessContext().values().removeIf(val -> (clazz.isAssignableFrom(val.getClass())));
    }

    /**
     * Contain key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean containKey(String key) {
        return hashMap.containsKey(key);
    }

    /**
     * Expired boolean.
     *
     * @return the boolean
     */
    public boolean expired() {
        if (expireTime > 0) {
            return System.currentTimeMillis() > expireTime;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (getChannelId() != null)
            stringBuilder.append("ChannelId=" + getChannelId() + System.lineSeparator());
        else
            stringBuilder.append("ChannelId=" + "null" + System.lineSeparator());

        stringBuilder.append("Msg=" + getMsg() + System.lineSeparator());

        return stringBuilder.toString();
    }

    /**
     * Gets tracer.
     *
     * @return the tracer
     */
    public MsgTracer getTracer() {
        return tracer;
    }

    /**
     * Gets elapse.
     *
     * @return the elapse
     */
    public long getElapse() {
        return elapse;
    }

    /**
     * Increase elapse long.
     *
     * @param addedElaps the added elaps
     * @return the long
     */
    public synchronized long increaseElapse(long addedElaps) {
        elapse = elapse + addedElaps;
        return elapse;
    }

    /**
     * Gets edge node id.
     *
     * @return the edge node id
     */
    public Integer getEdgeNodeId() {
        return edgeNodeId;
    }

    /**
     * Sets edge node id.
     *
     * @param edgeNodeId the edge node id
     * @return the edge node id
     */
    public MsgContext<M> setEdgeNodeId(Integer edgeNodeId) {
        this.edgeNodeId = edgeNodeId;
        return this;
    }

    private void addPassingNode(int nodeId) {
        if (passingNodes.size() == 0)
            passingNodes.add(nodeId);
        if (passingNodes.get(passingNodes.size() - 1) != nodeId)
            passingNodes.add(nodeId);
    }

    /**
     * Merge passing node.
     *
     * @param context the context
     */
    public void mergePassingNode(MsgContext context) {
        this.passingNodes.addAll(context.passingNodes);
    }

    /**
     * Get passing nodes integer [ ].
     *
     * @return the integer [ ]
     */
    public Integer[] getPassingNodes() {
        Integer[] list = new Integer[passingNodes.size()];
        passingNodes.toArray(list);
        return list;
    }


}
