package juja.microservices.gamification.dao;

import juja.microservices.WithoutScheduling;
import juja.microservices.gamification.entity.KeeperDTO;
import juja.microservices.gamification.exceptions.KeepersMicroserviceExchangeException;
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
public class RestKeeperRepositoryTest implements WithoutScheduling {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Inject
    private KeeperRepository keeperRepository;
    @Inject
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @Value("${keepers.endpoint.getKeepers}")
    private String keepersGetKeepersUrl;

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void getKeepers() {
        mockServer.expect(requestTo(keepersGetKeepersUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "[{\"uuid\":\"0002A\",\"directions\":[\"First direction\",\"Second direction\"]}," +
                                "{\"uuid\":\"0002B\",\"directions\":[\"Third direction\"]}]",
                        MediaType.APPLICATION_JSON));

        List<String> firstKeeperDirections = new ArrayList<>();
        List<String> secondKeeperDerections = new ArrayList<>();
        firstKeeperDirections.add("First direction");
        firstKeeperDirections.add("Second direction");
        secondKeeperDerections.add("Third direction");

        List<KeeperDTO> expectedList = new ArrayList<>();
        expectedList.add(new KeeperDTO("0002A", firstKeeperDirections));
        expectedList.add(new KeeperDTO("0002B", secondKeeperDerections));

        List<KeeperDTO> result = keeperRepository.getKeepers();

        assertThat(result, equalTo(expectedList));
    }

    @Test
    public void shouldThrowExceptionWhenUserServiceThrowExceptinon() {
        mockServer.expect(requestTo(keepersGetKeepersUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest().body("bad request"));

        thrown.expect(KeepersMicroserviceExchangeException.class);
        thrown.expectMessage(containsString("Keepers microservice Exchange Error: "));

        keeperRepository.getKeepers();
    }
}