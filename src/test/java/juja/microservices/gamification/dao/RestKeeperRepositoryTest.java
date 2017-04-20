package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.Keeper;
import juja.microservices.gamification.exceptions.UserMicroserviceExchangeException;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestKeeperRepositoryTest {

    @Inject
    private KeeperRepository keeperRepository;
    @Inject
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @Value("${user.baseURL}")
    private String urlBase;
    @Value("${endpoint.keepers}")
    private String urlGetKeepers;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void getKeepers() {
        //given
        mockServer.expect(requestTo(urlBase + urlGetKeepers))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "[{\"uuid\":\"AAA\",\"description\":\"codenjoy\",\"from\":\"alex\"}," +
                        "{\"uuid\":\"BBB\",\"description\":\"job\",\"from\":\"alex\"}]",
                        MediaType.APPLICATION_JSON));
        List<Keeper> expectedList = new ArrayList<>();
        expectedList.add(new Keeper("AAA", "codenjoy", "alex"));
        expectedList.add(new Keeper("BBB", "job", "alex"));
        //when
        List<Keeper> result = keeperRepository.getKeepers();

        //then
        assertThat(result, equalTo(expectedList));
    }

    @Test
    public void shouldThrowExceptionWhenUserServiceThrowExceptinon() {
        //given
        mockServer.expect(requestTo(urlBase + urlGetKeepers))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest().body("bad request"));
        //then
        thrown.expect(UserMicroserviceExchangeException.class);
        thrown.expectMessage(containsString("User microservice Exchange Error: "));

        //when
        keeperRepository.getKeepers();
    }
}