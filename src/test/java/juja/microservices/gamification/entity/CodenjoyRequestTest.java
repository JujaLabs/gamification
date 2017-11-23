package juja.microservices.gamification.entity;

import juja.microservices.WithoutScheduling;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CodenjoyRequestTest implements WithoutScheduling {

    @Test
    public void shouldReturnCodenjoyRequest() {
        String expectedUserFrom = "max";
        String expectedFirstPlaceUser = "alex";
        String expectedSecondPlaceUser = "jack";
        String expectedThirdPlaceUser = "tomas";

        CodenjoyRequest codenjoyRequest = new CodenjoyRequest("max", "alex", "jack", "tomas");

        assertNotNull(codenjoyRequest);
        assertEquals(expectedUserFrom, codenjoyRequest.getFrom());
        assertEquals(expectedFirstPlaceUser, codenjoyRequest.getFirstPlace());
        assertEquals(expectedSecondPlaceUser, codenjoyRequest.getSecondPlace());
        assertEquals(expectedThirdPlaceUser, codenjoyRequest.getThirdPlace());
    }

}
