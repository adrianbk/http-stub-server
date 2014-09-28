package com.github.adrianbk.stubby.service.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.adrianbk.stubby.model.StubMessage;
import com.github.adrianbk.stubby.model.StubRequest;
import com.github.adrianbk.stubby.service.model.MatchField.FieldType;
import com.github.adrianbk.stubby.service.model.MatchField.MatchType;

public class TextBodyPatternTest {

    private StubMessage request;

    @Before
    public void before() {
        request = new StubRequest();
        request.setHeader("Content-Type", "text/plain");
        request.setBody("foo");
    }

    private void assertRequestMatches(String patternStr) {
        TextBodyPattern pattern = new TextBodyPattern(patternStr);

        MatchField result = pattern.matches(request);

        assertEquals(FieldType.BODY, result.getFieldType());
        assertEquals(MatchType.MATCH, result.getMatchType());
    }

    private void assertRequestDoesNotMatch(String patternStr) {
        TextBodyPattern pattern = new TextBodyPattern(patternStr);

        MatchField result = pattern.matches(request);

        assertEquals(FieldType.BODY, result.getFieldType());
        assertEquals(MatchType.MATCH_FAILURE, result.getMatchType());
    }

    @Test
    public void testInvalidContentType() throws Exception {
        request.setHeader("Content-Type", "application/json");
        
        assertRequestDoesNotMatch("foo");
    }

    @Test
    public void testMatches() throws Exception {
        assertRequestMatches(".*");
        assertRequestMatches("f[o]+");
    }

    @Test
    public void testNoMatch() throws Exception {
        assertRequestDoesNotMatch("b..");
    }
    
    @Test
    public void testEquals() {
        assertEquals(new TextBodyPattern("foo.*"), new TextBodyPattern("foo.*"));
    }
    
}
