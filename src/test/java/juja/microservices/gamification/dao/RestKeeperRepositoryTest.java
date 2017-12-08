package juja.microservices.gamification.dao;

import feign.FeignException;
import juja.microservices.gamification.dao.impl.AchievementRepository;
import juja.microservices.gamification.entity.KeeperDTO;
import juja.microservices.gamification.exceptions.KeepersMicroserviceExchangeException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Ivan Shapovalov
 * @author Benjamin Novikov
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@SpringBootTest
public class RestKeeperRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Inject
    private KeeperRepository keeperRepository;

    @MockBean
    private KeeperClient keeperClient;

    @MockBean
    private AchievementRepository achievementRepository;

    @Test
    public void getKeepers_KeeperClientRequest_ReturnsListOfTwoKeeperDTOs() {
        //given
        List<KeeperDTO> expectedList = new ArrayList<>(Arrays.asList(
                new KeeperDTO("0002A", Arrays.asList("First direction","Second direction")),
                new KeeperDTO("0002B", Collections.singletonList("Third direction"))));
        when(keeperClient.getKeepers()).thenReturn(expectedList);

        //when
        List<KeeperDTO> result = keeperRepository.getKeepers();

        //then
        assertEquals(result.size(), 2);
        assertThat(result, equalTo(expectedList));
        verify(keeperClient).getKeepers();
        verifyNoMoreInteractions(keeperClient);
    }

    @Test(expected = KeepersMicroserviceExchangeException.class)
    public void getKeepers_ExceptionThrown_IsKeepersMicroserviceExchangeException() {
        //given
        FeignException feignException = mock(FeignException.class);
        when(keeperClient.getKeepers()).thenThrow(feignException);

        //when
        try {
            keeperRepository.getKeepers();
        }

        //then
        finally {
            verify(keeperClient).getKeepers();
            verifyNoMoreInteractions(keeperClient);
        }
    }
}