package juja.microservices.gamification.controller;

import juja.microservices.WithoutScheduling;
import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.entity.DailyRequest;
import juja.microservices.gamification.entity.InterviewRequest;
import juja.microservices.gamification.entity.TeamRequest;
import juja.microservices.gamification.entity.ThanksRequest;
import juja.microservices.gamification.entity.WelcomeRequest;
import juja.microservices.gamification.service.AchievementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AchievementController.class)
public class AchievementControllerTest implements WithoutScheduling {

    private static final String FIRST_ACHIEVEMENT_ID = "1";
    private static final String SECOND_ACHIEVEMENT_ID = "2";
    private static final String THIRD_ACHIEVEMENT_ID = "3";
    private static final String FOURTH_ACHIEVEMENT_ID = "4";
    private static final String ONE_ID = "[\"1\"]";
    private static final String TWO_ID = "[\"1\",\"2\"]";
    private static final String THREE_ID = "[\"1\",\"2\",\"3\"]";
    private static final String FOUR_ID = "[\"1\",\"2\",\"3\",\"4\"]";
    @Value("${endpoint.achievements.addDaily}")
    private String achievementsAddDailyUrl;
    @Value("${endpoint.achievements.addThanks}")
    private String achievementsAddThanksUrl;
    @Value("${endpoint.achievements.addCodenjoy}")
    private String achievementsAddCodenjoyUrl;
    @Value("${endpoint.achievements.addInterview}")
    private String achievementsAddInterviewUrl;
    @Value("${endpoint.achievements.addKeeperThanks}")
    private String achievementsAddKeeperThanksUrl;
    @Value("${endpoint.achievements.addWelcome}")
    private String achievementsAddWelcomeUrl;
    @Value("${endpoint.achievements.addTeam}")
    private String achievementsAddTeamUrl;
    @Inject
    private MockMvc mockMvc;

    @MockBean
    private AchievementService service;

    @Test
    public void addDaily() throws Exception {
        List<String> expectedList = Arrays.asList(FIRST_ACHIEVEMENT_ID);
        when(service.addDaily(any(DailyRequest.class))).thenReturn(expectedList);
        String jsonContentRequest = "{\"from\":\"max\",\"description\":\"Daily report\"}";

        String result = getResult(achievementsAddDailyUrl, jsonContentRequest);

        ArgumentCaptor<DailyRequest> captor = ArgumentCaptor.forClass(DailyRequest.class);
        verify(service).addDaily(captor.capture());
        assertEquals("max", captor.getValue().getFrom());
        assertEquals("Daily report", captor.getValue().getDescription());
        assertEquals(ONE_ID, result);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addDailyWithEmptyFrom() throws Exception {
        String jsonContentRequest = "{\"from\":\"\",\"description\":\"Daily report\"}";

        checkBadRequest(achievementsAddDailyUrl, jsonContentRequest);
    }

    @Test
    public void addDailyWithEmptyDescription() throws Exception {
        String jsonContentRequest = "{\"from\":\"sasha\",\"description\":\"\"}";

        checkBadRequest(achievementsAddDailyUrl, jsonContentRequest);
    }

    @Test
    public void addFirstThanks() throws Exception {
        List<String> ids = Arrays.asList(FIRST_ACHIEVEMENT_ID);
        when(service.addThanks(any(ThanksRequest.class))).thenReturn(ids);
        String jsonContentRequest = "{\"from\":\"max\",\"to\":\"john\",\"description\":\"thanks\"}";

        String result = getResult(achievementsAddThanksUrl, jsonContentRequest);

        ArgumentCaptor<ThanksRequest> captor = ArgumentCaptor.forClass(ThanksRequest.class);
        verify(service).addThanks(captor.capture());
        assertEquals("max", captor.getValue().getFrom());
        assertEquals("john", captor.getValue().getTo());
        assertEquals("thanks", captor.getValue().getDescription());
        assertEquals(ONE_ID, result);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addSecondThanks() throws Exception {
        List<String> ids = Arrays.asList(FIRST_ACHIEVEMENT_ID, SECOND_ACHIEVEMENT_ID);
        when(service.addThanks(any(ThanksRequest.class))).thenReturn(ids);
        String jsonContentRequest = "{\"from\":\"max\",\"to\":\"john\",\"description\":\"thanks\"}";

        String result = getResult(achievementsAddThanksUrl, jsonContentRequest);

        ArgumentCaptor<ThanksRequest> captor = ArgumentCaptor.forClass(ThanksRequest.class);
        verify(service).addThanks(captor.capture());
        assertEquals("max", captor.getValue().getFrom());
        assertEquals("john", captor.getValue().getTo());
        assertEquals("thanks", captor.getValue().getDescription());
        assertEquals(TWO_ID, result);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addThanksWithEmptyFrom() throws Exception {
        String jsonContentRequest = "{\"from\":\"\",\"to\":\"john\",\"description\":\"thanks\"}";

        checkBadRequest(achievementsAddThanksUrl, jsonContentRequest);
    }

    @Test
    public void addThanksWithEmptyTo() throws Exception {
        String jsonContentRequest = "{\"from\":\"max\",\"to\":\"\",\"description\":\"thanks\"}";

        checkBadRequest(achievementsAddThanksUrl, jsonContentRequest);
    }

    @Test
    public void addThanksWithEmptyDescription() throws Exception {
        String jsonContentRequest = "{\"from\":\"max\",\"to\":\"john\",\"description\":\"\"}";

        checkBadRequest(achievementsAddThanksUrl, jsonContentRequest);
    }

    @Test
    public void addThanksKeeper() throws Exception {
        List<String> ids = Arrays.asList(FIRST_ACHIEVEMENT_ID);
        when(service.addThanksKeeper()).thenReturn(ids);

        String result = getResult(achievementsAddKeeperThanksUrl, "");

        verify(service).addThanksKeeper();
        assertEquals(ONE_ID, result);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addCodenjoy() throws Exception {
        CodenjoyRequest expected = new CodenjoyRequest("max", "alex", "jack", "tomas");
        List<String> ids = Arrays.asList(FIRST_ACHIEVEMENT_ID, SECOND_ACHIEVEMENT_ID, THIRD_ACHIEVEMENT_ID);

        when(service.addCodenjoy(any(CodenjoyRequest.class))).thenReturn(ids);
        String jsonContentRequest =
                "{\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";

        String result = getResult(achievementsAddCodenjoyUrl, jsonContentRequest);

        ArgumentCaptor<CodenjoyRequest> captor = ArgumentCaptor.forClass(CodenjoyRequest.class);
        verify(service).addCodenjoy(captor.capture());
        assertEquals(expected.getFrom(), captor.getValue().getFrom());
        assertEquals(expected.getFirstPlace(), captor.getValue().getFirstPlace());
        assertEquals(expected.getSecondPlace(), captor.getValue().getSecondPlace());
        assertEquals(expected.getThirdPlace(), captor.getValue().getThirdPlace());
        assertEquals(THREE_ID, result);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addCodenjoyWithEmptyFrom() throws Exception {
        String jsonContentRequest =
                "{\"from\":\"\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";

        checkBadRequest(achievementsAddCodenjoyUrl, jsonContentRequest);
    }

    @Test
    public void addCodenjoyWithEmptyFirstPlace() throws Exception {
        String jsonContentRequest =
                "{\"from\":\"max\",\"firstPlace\":\"\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";

        checkBadRequest(achievementsAddCodenjoyUrl, jsonContentRequest);
    }

    @Test
    public void addCodenjoyWithEmptySecondPlace() throws Exception {
        String jsonContentRequest =
                "{\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"\",\"thirdPlace\":\"tomas\"}";

        checkBadRequest(achievementsAddCodenjoyUrl, jsonContentRequest);
    }

    @Test
    public void addCodenjoyWithEmptyThirdPlace() throws Exception {
        String jsonContentRequest =
                "{\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"\"}";

        checkBadRequest(achievementsAddCodenjoyUrl, jsonContentRequest);
    }

    @Test
    public void addInterview() throws Exception {
        List<String> expectedList = Arrays.asList(FIRST_ACHIEVEMENT_ID);
        InterviewRequest expected = new InterviewRequest("max", "Interview report");
        when(service.addInterview((any(InterviewRequest.class)))).thenReturn(expectedList);
        String jsonContentRequest = "{\"from\":\"max\",\"description\":\"Interview report\"}";

        String result = getResult(achievementsAddInterviewUrl, jsonContentRequest);

        ArgumentCaptor<InterviewRequest> captor = ArgumentCaptor.forClass(InterviewRequest.class);
        verify(service).addInterview(captor.capture());
        assertEquals(expected.getFrom(), captor.getValue().getFrom());
        assertEquals(expected.getDescription(), captor.getValue().getDescription());
        assertEquals(ONE_ID, result);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addInterviewWithEmptyFrom() throws Exception {
        String jsonContentRequest = "{\"from\":\"\",\"description\":\"Interview report\"}";

        checkBadRequest(achievementsAddInterviewUrl, jsonContentRequest);
    }

    @Test
    public void addInterviewWithEmptyDescription() throws Exception {
        String jsonContentRequest = "{\"from\":\"max\",\"description\":\"\"}";

        checkBadRequest(achievementsAddInterviewUrl, jsonContentRequest);
    }

    @Test
    public void addWelcome() throws Exception {
        List<String> ids = Arrays.asList(FIRST_ACHIEVEMENT_ID);
        String jsonContentRequest = "{\"from\":\"max\",\"to\":\"john\"}";
        when(service.addWelcome(any(WelcomeRequest.class))).thenReturn(ids);

        String result = getResult(achievementsAddWelcomeUrl, jsonContentRequest);

        ArgumentCaptor<WelcomeRequest> captor = ArgumentCaptor.forClass(WelcomeRequest.class);
        verify(service).addWelcome(captor.capture());
        assertEquals("max", captor.getValue().getFrom());
        assertEquals("john", captor.getValue().getTo());
        assertEquals(ONE_ID, result);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addWelcomeWithEmptyTo() throws Exception {
        String jsonContentRequest = "{\"from\":\"max\",\"to\":\"\"}";
        checkBadRequest(achievementsAddWelcomeUrl, jsonContentRequest);
    }

    @Test
    public void addTeam() throws Exception {
        List<String> ids = Arrays.asList(FIRST_ACHIEVEMENT_ID, SECOND_ACHIEVEMENT_ID, THIRD_ACHIEVEMENT_ID,
                FOURTH_ACHIEVEMENT_ID);
        when(service.addTeam(any(TeamRequest.class))).thenReturn(ids);
        final List<String> expectedUuids = Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4");
        String jsonContentRequest =
                "{\"from\":\"uuid1\",\"members\":[\"uuid1\",\"uuid2\",\"uuid3\",\"uuid4\"]}";

        String result = getResult(achievementsAddTeamUrl, jsonContentRequest);

        ArgumentCaptor<TeamRequest> captor = ArgumentCaptor.forClass(TeamRequest.class);
        verify(service).addTeam(captor.capture());
        assertEquals("uuid1", captor.getValue().getFrom());
        assertThat("List equality without order",
                captor.getValue().getMembers(), containsInAnyOrder(expectedUuids.toArray(new String[expectedUuids.size()])));
        assertEquals(FOUR_ID, result);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addTeamWithEmptyFrom() throws Exception {
        String jsonContentRequest = "{\"from\":\"\",\"members\":[\"uuid1\",\"uuid2\",\"uuid3\",\"uuid4\"]}";

        checkBadRequest(achievementsAddTeamUrl, jsonContentRequest);
    }

    @Test
    public void addTeamWithEmptyMembers() throws Exception {
        String jsonContentRequest = "{\"from\":\"uuid1\",\"members\":[]}";

        checkBadRequest(achievementsAddTeamUrl, jsonContentRequest);
    }

    private String getResult(String uri, String jsonContentRequest) throws Exception {
        return mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private void checkBadRequest(String uri, String jsonContentRequest) throws Exception {
        mockMvc.perform(post(uri)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}