package io.avreen.redis.common;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * The class Cli command output.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 */
public class CLICommandOutput<K, V> extends CommandOutput<K, V, List<Object>> {

    private boolean initialized;
    private Deque<Integer> counts = new ArrayDeque();
    private Deque<List<Object>> stack = new ArrayDeque();

    /**
     * Instantiates a new Cli command output.
     *
     * @param codec the codec
     */
    public CLICommandOutput(RedisCodec<K, V> codec) {
        super(codec, newList(0));
    }

    /**
     * New list list.
     *
     * @param <T>      the type parameter
     * @param capacity the capacity
     * @return the list
     */
    static <T> List<T> newList(int capacity) {
        return (List) (capacity < 1 ? new ArrayList() : new ArrayList(Math.max(1, capacity)));
    }

    public void set(ByteBuffer bytes) {
        if (bytes != null) {
            if (this.stack.isEmpty())
                this.stack.push(this.output);
            V value = this.codec.decodeValue(bytes);
            ((List) this.stack.peek()).add(value);
        }

    }

    public void set(long integer) {
        if (this.stack.isEmpty())
            this.stack.push(this.output);
        ( this.stack.peek()).add(Long.valueOf(integer));
    }

    public void complete(int depth) {
        if (!this.counts.isEmpty()) {
            if (depth == this.stack.size() && ((List) this.stack.peek()).size() == ((Integer) this.counts.peek()).intValue()) {
                List<Object> pop = (List) this.stack.pop();
                this.counts.pop();
                if (!this.stack.isEmpty()) {
                    (this.stack.peek()).add(pop);
                }
            }

        }
    }

    public void multi(int count) {
        if (!this.initialized) {
            this.output = newList(count);
            this.initialized = true;
        }

        if (this.stack.isEmpty()) {
            this.stack.push(this.output);
        } else {
            this.stack.push(newList(count));
        }

        this.counts.push(Integer.valueOf(count));
    }

}
