package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.TeamDTO;
import juja.microservices.gamification.exceptions.TeamsMicroserviceExchangeException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTeamRepositoryTest {

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Value("${teams.baseURL}")
    private String urlBase;
    @Value("${endpoint.teams.get.team.by.user.uuid}")
    private String urlGetTeamByUserUuid;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void getTeam() {
        //given
        mockServer.expect(requestTo(urlBase + urlGetTeamByUserUuid + "uuid1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"id\":\"123\",\"fakefield\":\"blablabla\",\"members\":[\"uuid1\",\"uuid2\",\"uuid3\",\"uuid4\"]}",
                        MediaType.APPLICATION_JSON));
        TeamDTO expectedTeam = new TeamDTO(new LinkedHashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4")));

        //when
        TeamDTO result = teamRepository.getTeamByUuid("uuid1");

        //then
        assertEquals(expectedTeam.getMembers(), result.getMembers());
    }

    @Test
    public void shouldThrowExceptionWhenUserServiceThrowExceptinon() {
        //given
        mockServer.expect(requestTo(urlBase + urlGetTeamByUserUuid + "uuid1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest().body("bad request"));

        //then
        thrown.expect(TeamsMicroserviceExchangeException.class);
        thrown.expectMessage(containsString("Teams microservice Exchange Error: "));

        //when
        teamRepository.getTeamByUuid("uuid1");
    }




}