package juja.microservices.gamification.controller;

import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.entity.DailyRequest;
import juja.microservices.gamification.entity.InterviewRequest;
import juja.microservices.gamification.entity.ThanksRequest;
import juja.microservices.gamification.service.AchievementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AchievementController.class)
public class AchievementControllerTest {

    private static final String FIRST_ACHIEVEMENT_ID = "1";
    private static final String SECOND_ACHIEVEMENT_ID = "2";
    private static final String THIRD_ACHIEVEMENT_ID = "3";

    @Inject
    private MockMvc mockMvc;

    @MockBean
    private AchievementService service;

    @Test
    public void addDaily() throws Exception {
        when(service.addDaily(any(DailyRequest.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest =
                "{\"from\":\"max\",\"description\":\"Daily report\"}";
        String content = getResult("/achieve/daily", jsonContentRequest);
        assertEquals(FIRST_ACHIEVEMENT_ID, content);
    }

    @Test
    public void addFirstThanks() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add(FIRST_ACHIEVEMENT_ID);
        when(service.addThanks(any(ThanksRequest.class))).thenReturn(ids);
        String jsonContentRequest =
                "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";
        String content = getResult("/achieve/thanks", jsonContentRequest);
        assertEquals("[\"1\"]", content);
    }

    @Test
    public void addSecondThanks() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add(FIRST_ACHIEVEMENT_ID);
        ids.add(SECOND_ACHIEVEMENT_ID);
        when(service.addThanks(any(ThanksRequest.class))).thenReturn(ids);
        String jsonContentRequest =
                "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";
        String content = getResult("/achieve/thanks", jsonContentRequest);
        assertEquals("[\"".concat(FIRST_ACHIEVEMENT_ID).concat("\",\"").concat(SECOND_ACHIEVEMENT_ID).
                concat("\"]"), content);
    }

    @Test
    public void addCodenjoy() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add(FIRST_ACHIEVEMENT_ID);
        ids.add(SECOND_ACHIEVEMENT_ID);
        ids.add(THIRD_ACHIEVEMENT_ID);
        when(service.addCodenjoy(any(CodenjoyRequest.class))).thenReturn(ids);
        String jsonContentRequest =
                "{\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";
        String content = getResult("/achieve/codenjoy", jsonContentRequest);
        assertEquals("[\"".concat(FIRST_ACHIEVEMENT_ID).concat("\",\"").concat(SECOND_ACHIEVEMENT_ID).
                concat("\",\"").concat(THIRD_ACHIEVEMENT_ID).concat("\"]"), content);
    }

    @Test
    public void addInterview() throws Exception {
        when(service.addInterview((any(InterviewRequest.class)))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest =
                "{\"from\":\"max\",\"description\":\"Interview report\"}";
        String content = getResult("/achieve/interview", jsonContentRequest);
        assertEquals(FIRST_ACHIEVEMENT_ID, content);
    }

    private String getResult(String uri, String jsonContentRequest) throws Exception {
        MvcResult result = mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }
}