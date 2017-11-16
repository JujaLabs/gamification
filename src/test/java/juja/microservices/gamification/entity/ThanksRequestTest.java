package juja.microservices.gamification.entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ThanksRequestTest {

    @Test
    public void shouldReturnThanks() {
        String expectedUserFrom = "peter";
        String expectedUserTo = "jon";
        String expectedDescription = "Thanks for helping";

        ThanksRequest thanksRequest = new ThanksRequest("peter", "jon", "Thanks for helping");

        assertNotNull(thanksRequest);
        assertEquals(expectedUserFrom, thanksRequest.getFrom());
        assertEquals(expectedUserTo, thanksRequest.getTo());
        assertEquals(expectedDescription, thanksRequest.getDescription());
    }
}
