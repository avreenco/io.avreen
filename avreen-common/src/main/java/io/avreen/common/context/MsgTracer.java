package io.avreen.common.context;

import io.avreen.common.util.MessageDiagnosticKey;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The class Msg tracer.
 */
public class MsgTracer implements Serializable {
    private String traceId;


    private String spanId;
    private HashMap<String, String> hashMap = new HashMap<>();

    private static ThreadLocal<MsgTracer> threadLocal = new InheritableThreadLocal<>();

    /**
     * Instantiates a new Msg tracer.
     */
    public MsgTracer() {
        this.traceId = ContextKeyUtil.genRandomString();
        this.spanId = traceId;
        inject(this);
    }

    /**
     * Instantiates a new Msg tracer.
     *
     * @param traceId the trace id
     */
    public MsgTracer(String traceId) {
        this.traceId = traceId;
        this.spanId = ContextKeyUtil.genRandomString();
        inject(this);
    }


    /**
     * Gets trace id.
     *
     * @return the trace id
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * Gets span id.
     *
     * @return the span id
     */
    public String getSpanId() {
        return spanId;
    }


    /**
     * Inject.
     */
    public void inject() {
        MsgTracer.inject(this);
    }

    /**
     * Inject.
     *
     * @param msgTracer the msg tracer
     */
    public static void inject(MsgTracer msgTracer) {
        if (msgTracer == null)
            return;
        threadLocal.set(msgTracer);
        injectKey("TraceId", msgTracer.getTraceId());
        injectKey("SpanId", msgTracer.getSpanId());
        for (String key : msgTracer.hashMap.keySet()) {
            injectKey(key, msgTracer.hashMap.get(key));
        }
    }

    /**
     * Eject.
     *
     * @param msgTracer the msg tracer
     */
    public static void eject(MsgTracer msgTracer) {
        if (msgTracer == null)
            return;
        threadLocal.remove();
        ejectKey("TraceId");
        for (String key : msgTracer.hashMap.keySet()) {
            ejectKey(key);
        }
    }

    private static void injectKey(String key, String value) {
        MessageDiagnosticKey.put("X-A3-" + key, value);
    }

    private static void ejectKey(String key) {
        MessageDiagnosticKey.remove("X-A3-" + key);
    }

    /**
     * Put msg tracer.
     *
     * @param key   the key
     * @param value the value
     * @return the msg tracer
     */
    public MsgTracer put(String key, String value) {
        hashMap.put(key, value);
        if (value != null)
            injectKey(key, value);
        return this;
    }

    /**
     * Current msg tracer.
     *
     * @return the msg tracer
     */
    public static MsgTracer current() {
        return threadLocal.get();
    }
}
