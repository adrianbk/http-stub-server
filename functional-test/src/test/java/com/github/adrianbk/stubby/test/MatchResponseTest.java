package com.github.adrianbk.stubby.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.github.adrianbk.stubby.test.support.TestBase;

public class MatchResponseTest extends TestBase {
    
    @Test
    public void testAllFields() {
        builder()
            .setRequestPath("/.*")
            .setResponseStatus(201)
            .addResponseHeader("X-Foo", "bar1")
            .addResponseHeader("X-Foo", "bar2") // two values for single name
            .addResponseHeader("x-foo", "bar3; bar4") // check case-insensitivity
            .setResponseBody("response body")
            .stub(); 
        
        GenericClientResponse response = client.executeGet("/test");
        
        assertEquals(201, response.getStatus());
        assertEquals(Arrays.asList("bar1", "bar2"), response.getHeaders("X-Foo"));
        assertEquals(Arrays.asList("bar3; bar4"), response.getHeaders("x-foo"));
        assertEquals("response body", response.getText());
    }

}
