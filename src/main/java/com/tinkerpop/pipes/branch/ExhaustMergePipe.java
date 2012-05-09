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
public class ExhaustMergePipe<S> extends AbstractMetaPipe<S, S> implements MetaPipe<S> {

    private final List<Pipe<?, S>> pipes;
    int current = 0;
    final int total;

    public ExhaustMergePipe(final List<Pipe<?, S>> pipes) {
        this.pipes = pipes;
        this.total = pipes.size();
    }

    public S processNextStart() {
        while (true) {
            final Pipe<?, S> pipe = this.pipes.get(this.current);
            if (pipe.peek() != null) {
                return (S) pipe.take();
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

    public List<Pipe<?, S>> getPipes() {
        return this.pipes;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }

}
