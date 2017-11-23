package juja.microservices.gamification.controller;

import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;
import juja.microservices.gamification.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_ATOM_XML;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String POINT_SUMS = "[{\"to\":\"max\",\"point\":5},{\"to\":\"john\",\"point\":3}]";
    private static final String ACHIEVEMENTS = "[" +
            "{\"user\":\"max\",\"details\":[" +
            "{\"from\":\"max\",\"to\":\"max\",\"point\":1,\"description\":\"Daily\"," +
            "\"type\":\"DAILY\",\"id\":null,\"sendDate\":\"2017-04-21\"}," +
            "{\"from\":\"john\",\"to\":\"max\",\"point\":1,\"description\":\"Thanks\"," +
            "\"type\":\"THANKS\",\"id\":null,\"sendDate\":\"2017-04-21\"}]}," +
            "{\"user\":\"john\",\"details\":[" +
            "{\"from\":\"john\",\"to\":\"john\",\"point\":10,\"description\":\"Interview\"," +
            "\"type\":\"INTERVIEW\",\"id\":null,\"sendDate\":\"2017-04-21\"}]}" +
            "]";
    @Value("${endpoint.users.getPointSum}")
    private String usersGetPointSum;
    @Value("${endpoint.users.getAchievementDetails}")
    private String usersGetAchievementDetails;
    @Inject
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    public void getAllUsersWithPointSum() throws Exception {
        List<UserPointsSum> list = new ArrayList<>();
        list.add(new UserPointsSum("max", 5));
        list.add(new UserPointsSum("john", 3));
        when(service.getAllUsersWithPointSum()).thenReturn(list);

        String result = mockMvc.perform(get(usersGetPointSum)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(POINT_SUMS, result);
        verify(service).getAllUsersWithPointSum();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void getUsersWithAchievementDetails() throws Exception {
        Achievement achievementOne = new Achievement("max", "max", 1, "Daily", AchievementType.DAILY);
        Achievement achievementTwo = new Achievement("john", "max", 1, "Thanks", AchievementType.THANKS);
        Achievement achievementThree = new Achievement("john", "john", 10, "Interview", AchievementType.INTERVIEW);
        achievementOne.setSendDate(testDate());
        achievementTwo.setSendDate(testDate());
        achievementThree.setSendDate(testDate());

        List<Achievement> achievementsFirstUser = Arrays.asList(achievementOne, achievementTwo);
        List<Achievement> achievementsSecondUser = Arrays.asList(achievementThree);

        List<UserAchievementDetails> achievements = new ArrayList<>();
        achievements.add(new UserAchievementDetails("max", achievementsFirstUser));
        achievements.add(new UserAchievementDetails("john", achievementsSecondUser));
        when(service.getUserAchievementsDetails(any(UserIdsRequest.class))).thenReturn(achievements);

        String jsonContentRequest = "{\"toIds\":[\"max\",\"john\"]}";
        String result = mockMvc.perform(post(usersGetAchievementDetails)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ArgumentCaptor<UserIdsRequest> captor = ArgumentCaptor.forClass(UserIdsRequest.class);
        verify(service).getUserAchievementsDetails(captor.capture());
        assertEquals(Arrays.asList("max", "john"), captor.getValue().getToIds());
        assertEquals(ACHIEVEMENTS, result);
        verifyNoMoreInteractions(service);
    }

    private LocalDateTime testDate() {
        return LocalDateTime.of(2017, Month.APRIL, 21, 12, 0);
    }

    @Test()
    public void getHttpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(post(usersGetPointSum)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test()
    public void getHttpMediaTypeNotSupportedException() throws Exception {
        mockMvc.perform(post(usersGetAchievementDetails)
                .contentType(APPLICATION_ATOM_XML))
                .andExpect(status().isUnsupportedMediaType());
    }
}