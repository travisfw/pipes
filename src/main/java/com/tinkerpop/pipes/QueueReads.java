package com.tinkerpop.pipes;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The read methods from {@link BlockingQueue}.
 * 
 * @author travis@traviswellman.com
 *
 * @param <E>
 */
public interface QueueReads<E> {
    E take() throws InterruptedException;
    E poll(long timeout, TimeUnit unit)
        throws InterruptedException;
    boolean remove(Object o);
    public boolean contains(Object o);
    int drainTo(Collection<? super E> c);
    int drainTo(Collection<? super E> c, int maxElements);
}
