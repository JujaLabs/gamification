package juja.microservices.gamification.controller;

import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.service.UserService;
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
            "\"type\":\"DAILY\",\"id\":null,\"sendDate\":\"2017-02-28\"}," +
            "{\"from\":\"john\",\"to\":\"max\",\"point\":1,\"description\":\"Thanks\"," +
            "\"type\":\"THANKS\",\"id\":null,\"sendDate\":\"2017-02-28\"}]}," +
            "{\"user\":\"john\",\"details\":[" +
            "{\"from\":\"john\",\"to\":\"john\",\"point\":10,\"description\":\"Interview\"," +
            "\"type\":\"INTERVIEW\",\"id\":null,\"sendDate\":\"2017-02-28\"}]}" +
            "]";

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
        String result = mockMvc.perform(get("/user/pointSum")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(POINT_SUMS, result);
    }

    @Test
    public void getUsersWithAchievementDetails() throws Exception {
        Achievement achievementOne = new Achievement("max", "max", 1, "Daily", AchievementType.DAILY);
        Achievement achievementTwo = new Achievement("john", "max", 1, "Thanks", AchievementType.THANKS);
        Achievement achievementThree = new Achievement("john", "john", 10, "Interview", AchievementType.INTERVIEW);
        achievementOne.setSendDate("2017-02-28");
        achievementTwo.setSendDate("2017-02-28");
        achievementThree.setSendDate("2017-02-28");
        List<Achievement> achievementsFirstUser = new ArrayList<>();
        achievementsFirstUser.add(achievementOne);
        achievementsFirstUser.add(achievementTwo);
        List<Achievement> achievementsSecondUser = new ArrayList<>();
        achievementsSecondUser.add(achievementThree);
        List<UserAchievementDetails> achievements = new ArrayList<>();
        achievements.add(new UserAchievementDetails("max", achievementsFirstUser));
        achievements.add(new UserAchievementDetails("john", achievementsSecondUser));
        when(service.getUserAchievementsDetails(any(UserIdsRequest.class))).thenReturn(achievements);

        String jsonContentRequest = "{\"toIds\":[\"max\",\"john\"]}";
        String result = mockMvc.perform(post("/user/achieveDetails")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(ACHIEVEMENTS, result);
    }

    @Test()
    public void getHttpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(post("/user/pointSum")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test()
    public void getHttpMediaTypeNotSupportedException() throws Exception {
        mockMvc.perform(get("/user/pointSum")
                .contentType(APPLICATION_ATOM_XML))
                .andExpect(status().isUnsupportedMediaType());
    }
}