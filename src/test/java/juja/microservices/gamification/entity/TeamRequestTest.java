package juja.microservices.gamification.entity;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TeamRequestTest {

    @Test
    public void shouldReturnTeamRequest() {
        String expectedUserFrom = "uuid1";
        Set<String> expectedMembers = new HashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4"));

        TeamRequest teamRequest = new TeamRequest("uuid1",
                new HashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4")));

        assertNotNull(teamRequest);
        assertEquals(expectedUserFrom, teamRequest.getFrom());
        assertEquals(expectedMembers, teamRequest.getMembers());
    }

}
