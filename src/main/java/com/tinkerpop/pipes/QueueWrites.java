package com.tinkerpop.pipes;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The write methods from {@link BlockingQueue}.
 * 
 * @author travis
 *
 * @param <E>
 */
public interface QueueWrites<E> {
    boolean add(E e);
    boolean offer(E e);
    void put(E e) throws InterruptedException;
    boolean offer(E e, long timeout, TimeUnit unit)
            throws InterruptedException;
    int remainingCapacity();

}
