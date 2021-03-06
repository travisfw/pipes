package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PipelineTest extends TestCase {

    public void testPipelineReset() {
        Collection<String> names = Arrays.asList("marko", "peter");
        Pipe<String, String> pipe1 = new IdentityPipe<String>();
        Pipe<String, String> pipe2 = new IdentityPipe<String>();
        Pipe<String, String> pipe3 = new IdentityPipe<String>();
        Pipe<String, String> pipeline = new Pipeline<String, String>(pipe1, pipe2, pipe3);
        pipeline.setStarts(names);

        assertTrue(pipeline.hasNext());
        pipeline.reset();
        assertTrue(pipeline.hasNext());
        pipeline.reset();
        assertFalse(pipeline.hasNext()); // Pipe has consumed and reset has thrown away both items.
    }
}
