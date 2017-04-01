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
    private static final String ONE_ID = "[\"1\"]";
    private static final String TWO_ID = "[\"1\",\"2\"]";
    private static final String THREE_ID = "[\"1\",\"2\",\"3\"]";

    @Inject
    private MockMvc mockMvc;

    @MockBean
    private AchievementService service;

    @Test
    public void addDaily() throws Exception {
        when(service.addDaily(any(DailyRequest.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest =
                "{\"from\":\"max\",\"description\":\"Daily report\"}";
        String result = getResult("/achieve/daily", jsonContentRequest);
        assertEquals(FIRST_ACHIEVEMENT_ID, result);
    }

    @Test
    public void addDailyWithoutFrom() throws Exception {
        when(service.addDaily(any(DailyRequest.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest = "{\"from\":\"\",\"description\":\"Daily report\"}";
        mockMvc.perform(post("/achieve/daily")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addDailyWithoutDescription() throws Exception {
        when(service.addDaily(any(DailyRequest.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest = "{\"from\":\"sasha\",\"description\":\"\"}";
        mockMvc.perform(post("/achieve/daily")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addFirstThanks() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add(FIRST_ACHIEVEMENT_ID);
        when(service.addThanks(any(ThanksRequest.class))).thenReturn(ids);
        String jsonContentRequest =
                "{\"from\":\"max\",\"to\":\"john\",\"description\":\"thanks\"}";
        String result = getResult("/achieve/thanks", jsonContentRequest);
        assertEquals(ONE_ID, result);
    }

    @Test
    public void addSecondThanks() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add(FIRST_ACHIEVEMENT_ID);
        ids.add(SECOND_ACHIEVEMENT_ID);
        when(service.addThanks(any(ThanksRequest.class))).thenReturn(ids);
        String jsonContentRequest =
                "{\"from\":\"max\",\"to\":\"john\",\"description\":\"thanks\"}";
        String result = getResult("/achieve/thanks", jsonContentRequest);
        assertEquals(TWO_ID, result);
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
        String result = getResult("/achieve/codenjoy", jsonContentRequest);
        assertEquals(THREE_ID, result);
    }

    @Test
    public void addCodenjoyWithoutFrom() throws Exception {
        when(service.addDaily(any(DailyRequest.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest = "{\"from\":\"\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";
        mockMvc.perform(post("/achieve/codenjoy")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCodenjoyWithoutFirst() throws Exception {
        when(service.addDaily(any(DailyRequest.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest = "{\"from\":\"max\",\"firstPlace\":\"\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";
        mockMvc.perform(post("/achieve/codenjoy")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCodenjoyWithoutSecond() throws Exception {
        when(service.addDaily(any(DailyRequest.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest = "{\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"\",\"thirdPlace\":\"tomas\"}";
        mockMvc.perform(post("/achieve/codenjoy")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCodenjoyWithoutThird() throws Exception {
        when(service.addDaily(any(DailyRequest.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest = "{\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"\"}";
        mockMvc.perform(post("/achieve/codenjoy")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addInterview() throws Exception {
        when(service.addInterview((any(InterviewRequest.class)))).thenReturn(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest =
                "{\"from\":\"max\",\"description\":\"Interview report\"}";
        String result = getResult("/achieve/interview", jsonContentRequest);
        assertEquals(FIRST_ACHIEVEMENT_ID, result);
    }

    private String getResult(String uri, String jsonContentRequest) throws Exception {
        return mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private String getBadResult(String uri, String jsonContentRequest) throws Exception {
        return mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }
}