package juja.microservices.gamification.entity;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TeamTest {

    @Test
    public void shouldReturnTeam() {
        Set<String> expectedMembers = new HashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4"));
        Team team = new Team(expectedMembers);
        assertNotNull(team);
        assertEquals(expectedMembers, team.getMembers());
    }
}