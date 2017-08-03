package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.TeamRepository;
import juja.microservices.gamification.entity.TeamDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(TeamService.class)
public class TeamServiceTest {

    @Inject
    private TeamService service;

    @MockBean
    private TeamRepository repository;

    @Test
    public void getTeamByUuid(){
        //given
        TeamDTO expectedTeam = new TeamDTO(new LinkedHashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4")));
        System.out.println(expectedTeam.getMembers());

        //when
        when(repository.getTeamByUuid("uuid")).thenReturn(expectedTeam);
        TeamDTO actualTeam = service.getTeamByUuid("uuid");

        //then
        assertEquals(expectedTeam, actualTeam);
    }
}