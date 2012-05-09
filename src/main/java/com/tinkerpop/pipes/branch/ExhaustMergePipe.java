package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * ExhaustiveMergePipe will drain its first internal pipe, then its second, so on until all internal pipes are drained.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExhaustMergePipe<S> extends AbstractMetaPipe<S, S> implements MetaPipe {

    private final List<Pipe> pipes;
    int current = 0;
    final int total;

    public ExhaustMergePipe(final List<Pipe> pipes) {
        this.pipes = pipes;
        this.total = pipes.size();
    }

    public S processNextStart() {
        while (true) {
            final Pipe pipe = this.pipes.get(this.current);
            if (pipe.peek()) {
                return (S) pipe.next();
            } else {
                this.current = (this.current + 1) % this.total;
                if (this.current == 0) {
                    throw new NoSuchElementException();
                }
            }
        }
    }

    public List getCurrentPath() {
        if (this.pathEnabled)
            return this.pipes.get(this.current).getCurrentPath();
        else
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
    }

    public List<Pipe> getPipes() {
        return this.pipes;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }

    @Override
    public S take() throws InterruptedException {
        // TODO Auto-generated method stub
        throw new Error("unimplemented");
    }

    @Override
    public S poll(long timeout, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new Error("unimplemented");
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        throw new Error("unimplemented");
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        throw new Error("unimplemented");
    }

    @Override
    public int drainTo(Collection<? super S> c) {
        // TODO Auto-generated method stub
        throw new Error("unimplemented");
    }

    @Override
    public int drainTo(Collection<? super S> c, int maxElements) {
        // TODO Auto-generated method stub
        throw new Error("unimplemented");
    }
}
