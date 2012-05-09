package com.tinkerpop.pipes;

import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.iterators.HistoryIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An AbstractPipe provides most of the functionality that is repeated in every instance of a Pipe.
 * Any subclass of AbstractPipe should simply implement processNextStart(). The standard model is
 * <pre>
 * protected E processNextStart() throws NoSuchElementException {
 *   S s = this.starts.next();
 *   E e = // do something with the S to yield an E
 *   return e;
 * }
 * </pre>
 * If the current incoming S is not to be emitted and there are no other S objects to process and emit, then throw a NoSuchElementException.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractPipe<S, E> implements Pipe<S, E> {

    protected QueueReads<S> starts;
    private E nextEnd;
    protected E currentEnd;
    private boolean available = false;
    protected boolean pathEnabled = false;

    @Override
    public void setStarts(final QueueReads<S> starts) {
        if (starts instanceof Pipe) {
            this.starts = starts;
        } else {
            this.starts = new HistoryIterator<S>(starts);
        }
    }

    public void reset() {
        if (this.starts instanceof Pipe) {
            @SuppressWarnings("unchecked")
            Pipe<?, S> p = (Pipe<?, S>) this.starts;
            p.reset();
        }

        this.nextEnd = null;
        this.currentEnd = null;
        this.available = false;
    }

    public List<E> getCurrentPath() {
        if (this.pathEnabled) {
            final List<E> pathElements = getPathToHere();
            final int size = pathElements.size();
            // do not repeat filters as they dup the object
            if (size == 0 || pathElements.get(size - 1) != this.currentEnd) {
                pathElements.add(this.currentEnd);
            }
            return pathElements;
        } else {
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
        }
    }

    @Override
    public E remove() {
        if (this.available) {
            this.available = false;
            return (this.currentEnd = this.nextEnd);
        } else {
            return (this.currentEnd = this.processNextStart());
        }
    }

    public boolean hasNext() {
        if (this.available)
            return true;
        else {
            try {
                this.nextEnd = this.processNextStart();
                return (this.available = true);
            } catch (final NoSuchElementException e) {
                return (this.available = false);
            }
        }
    }

    public void enablePath(final boolean enable) {
        this.pathEnabled = enable;
        if (this.starts instanceof Pipe) {
            @SuppressWarnings("unchecked")
            Pipe<?, S> p = (Pipe<?, S>) this.starts;
            p.enablePath(enable);
        }
        else throw new IllegalStateException("this.starts is not a Pipe");
    }

    public String toString() {
        return PipeHelper.makePipeString(this);
    }

    protected abstract E processNextStart() throws NoSuchElementException;

    protected List<E> getPathToHere() {
        if (this.starts instanceof Pipe) {
            @SuppressWarnings("unchecked")
            Pipe<?, E> p = (Pipe<?, E>) this.starts;
            return p.getCurrentPath();
        } else if (this.starts instanceof HistoryIterator) {
            final List<E> list = new ArrayList<E>();
            list.add((E) ((HistoryIterator) this.starts).getLast());
            return list;
        } else {
            return new ArrayList<E>();
        }
    }


    @Override
    public int drainTo(final Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        int count = 0;
        for (E i : this) {
            if (count >= maxElements)
                break;
            c.add(i);
            count++;
        }
        return count; // HA ha ha ha haaaa
    }

}

