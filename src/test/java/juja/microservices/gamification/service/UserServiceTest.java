package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(UserService.class)
public class UserServiceTest {

    @Inject
    private MockMvc mockMvc;

    @Inject
    private UserService service;

    @MockBean
    private AchievementRepository repository;

    @Test
    public void getUserAchievementsDetails() throws Exception {
        List<UserAchievementDetails> detailsList = new ArrayList<>();
        detailsList.add(mock(UserAchievementDetails.class));
        when(repository.getUserAchievementsDetails(any(UserIdsRequest.class))).thenReturn(detailsList);
        UserIdsRequest request = new UserIdsRequest();
        List<UserAchievementDetails> details = service.getUserAchievementsDetails(request);
        assertEquals(detailsList, details);
    }

    @Test
    public void getAllUsersWithPointSum() throws Exception {
        List<UserPointsSum> pointsSumList = new ArrayList<>();
        pointsSumList.add(mock(UserPointsSum.class));
        when(repository.getAllUsersWithPointSum()).thenReturn(pointsSumList);
        List<UserPointsSum> points = service.getAllUsersWithPointSum();
        assertEquals(pointsSumList, points);
    }

    @Test(expected = IllegalStateException.class)
    public void getEmptyAllUsersWithPointSum() throws Exception {
        List<UserPointsSum> pointsSumList = new ArrayList<>();
        when(repository.getAllUsersWithPointSum()).thenReturn(pointsSumList);
        service.getAllUsersWithPointSum();
        fail();
    }
}