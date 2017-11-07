package juja.microservices.gamification.service;

import juja.microservices.WithoutScheduling;
import juja.microservices.gamification.dao.impl.AchievementRepository;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(UserService.class)
public class UserServiceTest implements WithoutScheduling {

    @Inject
    private UserService service;

    @MockBean
    private AchievementRepository repository;

    @Test
    public void getUserAchievementsDetails() throws Exception {
        List<UserAchievementDetails> expectedList = new ArrayList<>();
        expectedList.add(mock(UserAchievementDetails.class));
        when(repository.getUserAchievementsDetails(any(UserIdsRequest.class))).thenReturn(expectedList);
        UserIdsRequest request = new UserIdsRequest();

        List<UserAchievementDetails> actualList = service.getUserAchievementsDetails(request);

        assertEquals(expectedList, actualList);
        verify(repository).getUserAchievementsDetails(any(UserIdsRequest.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getAllUsersWithPointSum() throws Exception {
        List<UserPointsSum> expectedList = new ArrayList<>();
        expectedList.add(mock(UserPointsSum.class));
        when(repository.getAllUsersWithPointSum()).thenReturn(expectedList);

        List<UserPointsSum> actualList = service.getAllUsersWithPointSum();

        assertEquals(expectedList, actualList);
        verify(repository).getAllUsersWithPointSum();
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = IllegalStateException.class)
    public void getEmptyAllUsersWithPointSum() throws Exception {
        List<UserPointsSum> expectedList = new ArrayList<>();
        when(repository.getAllUsersWithPointSum()).thenReturn(expectedList);

        service.getAllUsersWithPointSum();
    }
}