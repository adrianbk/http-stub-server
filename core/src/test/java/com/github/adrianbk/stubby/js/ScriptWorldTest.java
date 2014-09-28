package com.github.adrianbk.stubby.js;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.github.adrianbk.stubby.model.StubExchange;
import com.github.adrianbk.stubby.model.StubRequest;
import com.github.adrianbk.stubby.model.StubResponse;

public class ScriptWorldTest {

    private StubRequest request;
    private StubExchange response;
    
    @Before
    public void before() {
        request = new StubRequest();
        request.setPath("/request/path");
        
        response = new StubExchange();
        response.setResponse(new StubResponse());
        response.setRequest(new StubRequest());
        response.setDelay(123L);
    }
    
    @Test
    public void testCopiesFields() {
        ScriptWorld world = new ScriptWorld(request, response);
        
        assertTrue(world.getRequest() != response.getRequest()); // should be different instances
        assertTrue(world.getResponse() != response.getResponse());
        
        assertEquals("/request/path", world.getRequest().getPath()); // ensure we use the acutal request, not request pattern

        assertTrue(world.getDelay().equals(response.getDelay())); // immutable anyway...
    }
    
}
