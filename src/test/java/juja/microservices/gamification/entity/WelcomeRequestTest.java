package juja.microservices.gamification.entity;

import juja.microservices.WithoutScheduling;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WelcomeRequestTest implements WithoutScheduling {

    @Test
    public void shouldReturnWelcome() {
        String expectedUserFrom = "peter";
        String expectedUserTo = "john";

        WelcomeRequest welcomeRequest = new WelcomeRequest("peter", "john");

        assertNotNull(welcomeRequest);
        assertEquals(expectedUserFrom, welcomeRequest.getFrom());
        assertEquals(expectedUserTo, welcomeRequest.getTo());
    }
}