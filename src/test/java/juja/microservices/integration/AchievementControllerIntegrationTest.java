package juja.microservices.integration;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.dao.KeeperRepository;
import juja.microservices.gamification.entity.KeeperDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author danil.kuznetsov
 * @author Vadim Dyachenko
 */
@RunWith(SpringRunner.class)
public class AchievementControllerIntegrationTest extends BaseIntegrationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final String ACHIEVEMENTS_ADD_DAILY_URL = "/v1/gamification/achievements/daily";
    private static final String ACHIEVEMENTS_ADD_THANKS_URL = "/v1/gamification/achievements/thanks";
    private static final String ACHIEVEMENTS_ADD_CODENJOY_URL = "/v1/gamification/achievements/codenjoy";
    private static final String ACHIEVEMENTS_ADD_INTERVIEW_URL = "/v1/gamification/achievements/interview";
    private static final String ACHIEVEMENTS_ADD_KEEPER_THANKS_URL = "/v1/gamification/achievements/keepers/thanks";
    private static final String ACHIEVEMENTS_ADD_WELCOME_URL = "/v1/gamification/achievements/welcome";
    private static final String ACHIEVEMENTS_ADD_TEAM_URL = "/v1/gamification/achievements/team";
    private static final String ACHIEVEMENTS_ADD_ADMIN = "/v1/gamification/achievements/admin";
    private static final String USERS_GET_POINT_SUM = "/v1/gamification/users/pointSum";
    private static final String USERS_GET_ACHIEVEMENT_DETAILS = "/v1/gamification/users/achievementDetails";

    private MockMvc mockMvc;

    @MockBean
    private KeeperRepository keeperRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void getAllUsersWithAchievementAndReturnJson() throws Exception {
        mockMvc.perform(get(USERS_GET_POINT_SUM)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getUsersAchievementDetailsAndReturnJson() throws Exception {
        String json = "{\"toIds\":[\"sasha\", \"ira\"]}";
        mockMvc.perform(post(USERS_GET_ACHIEVEMENT_DETAILS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksShouldReturnValidJson() throws Exception {
        String expectedJson = "[{\"to\":\"ira\",\"point\":1}]";
        String jsonContentRequest = "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"good work\"}";

        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAdminThanksShouldReturnValidJson() throws Exception {
        String expectedJson =
                "[{\"to\":\"ira\",\"point\":1}]";
        String jsonContentRequest =
                "{\"type\":\"THANKS\",\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"good work\"}";

        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_ADMIN);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksShouldReturnExceptionCannotThankYourself() throws Exception {
        String jsonContentRequest = "{\"from\":\"sasha\",\"to\":\"sasha\",\"description\":\"thanks\"}";

        addAchievementsFailed(jsonContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksShouldReturnExceptionOneThanksOnePersonInDay() throws Exception {
        String firstContentRequest = "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";
        String secondContentRequest = "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";

        addAchievementsIsOk(firstContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);
        addAchievementsFailed(secondContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addNewThanksShouldReturnExceptionNotMoreTwoThanks() throws Exception {
        String firstContentRequest = "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";
        String secondContentRequest = "{\"from\":\"sasha\",\"to\":\"max\",\"description\":\"thanks\"}";
        String thirdContentRequest = "{\"from\":\"sasha\",\"to\":\"peter\",\"description\":\"thanks\"}";

        addAchievementsIsOk(firstContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);
        addAchievementsIsOk(secondContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);
        addAchievementsFailed(thirdContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addNewTwoThanksShouldReturnOneForMe() throws Exception {
        String expectedJson =
                "[{\"to\":\"max\",\"point\":1}," +
                        "{\"to\":\"sasha\",\"point\":1}," +
                        "{\"to\":\"ira\",\"point\":1}]";
        String firstContentRequest =
                "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";
        String secondContentRequest =
                "{\"from\":\"sasha\",\"to\":\"max\",\"description\":\"thanks\"}";

        addAchievementsIsOk(firstContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);
        addAchievementsIsOk(secondContentRequest, ACHIEVEMENTS_ADD_THANKS_URL);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, content, false);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addCodenjoyReturnValidJson() throws Exception {
        String jsonContentRequest =
                "{\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";
        String expectedJson =
                "[{\"to\":\"alex\",\"point\":5},{\"to\":\"jack\",\"point\":3},{\"to\":\"tomas\",\"point\":1}]";
        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_CODENJOY_URL);
        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, content, false);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAdminCodenjoyReturnValidJson() throws Exception {
        String jsonContentRequest =
                "{\"type\":\"CODENJOY\",\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";
        String expectedJson =
                "[{\"to\":\"alex\",\"point\":5},{\"to\":\"jack\",\"point\":3},{\"to\":\"tomas\",\"point\":1}]";
        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_ADMIN);
        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, content, false);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addCodenjoyShouldReturnExceptionFromMissed() throws Exception {
        String jsonContentRequest =
                "{\"from\":\"\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";
        addAchievementsFailed(jsonContentRequest, ACHIEVEMENTS_ADD_CODENJOY_URL);
    }

    private MvcResult getMvcResultUserAchieveSum() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(USERS_GET_POINT_SUM)
                .contentType(APPLICATION_JSON_UTF8)).andReturn();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addInterviewReturnValidJson() throws Exception {
        String jsonContentRequest = "{\"from\":\"sasha\",\"description\":\"interview report\"}";
        String expectedJson = "[{\"to\":\"sasha\",\"point\":10}]";

        mockMvc.perform(post(ACHIEVEMENTS_ADD_INTERVIEW_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();

        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAdminInterviewReturnValidJson() throws Exception {
        String jsonContentRequest = "{\"type\":\"INTERVIEW\",\"from\":\"sasha\",\"description\":\"interview report\"}";
        String expectedJson = "[{\"to\":\"sasha\",\"point\":10}]";

        mockMvc.perform(post(ACHIEVEMENTS_ADD_ADMIN)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();

        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addInterviewShouldReturnExceptionFromMissed() throws Exception {
        String jsonContentRequest = "{\"from\":\"sasha\",\"description\":\"\"}";

        mockMvc.perform(post(ACHIEVEMENTS_ADD_INTERVIEW_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addDailyReturnValidJson() throws Exception {
        String jsonContentRequest = "{\"from\":\"sasha\",\"description\":\"text\"}";
        String expectedJson = "[{\"to\":\"sasha\",\"point\":1}]";

        mockMvc.perform(post(ACHIEVEMENTS_ADD_DAILY_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();

        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAdminDailyReturnValidJson() throws Exception {
        String jsonContentRequest = "{\"type\":\"DAILY\",\"from\":\"sasha\",\"description\":\"text\"}";
        String expectedJson = "[{\"to\":\"sasha\",\"point\":1}]";

        mockMvc.perform(post(ACHIEVEMENTS_ADD_ADMIN)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();

        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addSecondDailyReturnValidJson() throws Exception {
        String expectedJson = "[{\"to\":\"sasha\",\"point\":1}]";
        String firstContentRequest = "{\"from\":\"sasha\",\"description\":\"text\"}";
        String secondContentRequest = "{\"from\":\"sasha\",\"description\":\"add text\"}";

        addAchievementsIsOk(firstContentRequest, ACHIEVEMENTS_ADD_DAILY_URL);
        addAchievementsIsOk(secondContentRequest, ACHIEVEMENTS_ADD_DAILY_URL);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, content, false);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addDailyShouldReturnExceptionFromMissed() throws Exception {
        String jsonContentRequest = "{\"from\":\"\",\"description\":\"daily description\"}";

        mockMvc.perform(post(ACHIEVEMENTS_ADD_DAILY_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addDailyShouldReturnExceptionDescriptionMissed() throws Exception {
        String jsonContentRequest = "{\"from\":\"sasha\",\"description\":\"\"}";

        mockMvc.perform(post(ACHIEVEMENTS_ADD_DAILY_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addWelcomeShouldReturnValidJson() throws Exception {
        String expectedJson = "[{\"to\":\"john\",\"point\":1}]";
        String jsonContentRequest = "{\"from\":\"max\",\"to\":\"john\"}";

        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_WELCOME_URL);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAdminWelcomeShouldReturnValidJson() throws Exception {
        String expectedJson =
                "[{\"to\":\"john\",\"point\":1}]";
        String jsonContentRequest =
                "{\"type\":\"WELCOME\",\"from\":\"max\",\"to\":\"john\"}";

        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_ADMIN);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksKeeperShouldReturnValidJson() throws Exception {
        String expectedJson = "[{\"to\":\"0002A\",\"point\":2}]";

        List<String> directions = Arrays.asList("direction");
        KeeperDTO keeper = new KeeperDTO("0002A", directions);
        List<KeeperDTO> keepers = Arrays.asList(keeper);
        when(keeperRepository.getKeepers()).thenReturn(keepers);

        addAchievementsIsOk("", ACHIEVEMENTS_ADD_KEEPER_THANKS_URL);
        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();

        assertEquals(expectedJson, content);
        verify(keeperRepository).getKeepers();
        verifyNoMoreInteractions(keeperRepository);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAdminThanksKeeperShouldReturnValidJson() throws Exception {
        String expectedJson =
                "[{\"to\":\"0002A\",\"point\":2}]";

        List<KeeperDTO> keepers = new ArrayList<>();
        List<String> directions = new ArrayList<>();
        directions.add("direction");
        KeeperDTO keeper = new KeeperDTO("0002A", directions);
        keepers.add(keeper);
        when(keeperRepository.getKeepers()).thenReturn(keepers);

        addAchievementsIsOk("{\"type\":\"THANKS_KEEPER\"}", ACHIEVEMENTS_ADD_ADMIN);
        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();

        assertEquals(expectedJson, content);
        verify(keeperRepository).getKeepers();
        verifyNoMoreInteractions(keeperRepository);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addTeamShouldReturnValidJson() throws Exception {
        String jsonContentRequest = "{\"from\":\"uuid1\",\"members\":[\"uuid1\",\"uuid2\",\"uuid3\",\"uuid4\"]}";
        String expectedJson = "[{\"to\":\"uuid1\",\"point\":6},{\"to\":\"uuid2\",\"point\":6}," +
                "{\"to\":\"uuid3\",\"point\":6},{\"to\":\"uuid4\",\"point\":6}]";

        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_TEAM_URL);
        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();

        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAdminTeamShouldReturnValidJson() throws Exception {
        String jsonContentRequest =
                "{\"type\":\"TEAM\",\"from\":\"uuid1\",\"members\":[\"uuid1\",\"uuid2\",\"uuid3\",\"uuid4\"]}";
        String expectedJson = "[{\"to\":\"uuid1\",\"point\":6},{\"to\":\"uuid2\",\"point\":6}," +
                "{\"to\":\"uuid3\",\"point\":6},{\"to\":\"uuid4\",\"point\":6}]";

        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_ADMIN);
        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();

        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addTeamShouldReturnTeamAchievementException() throws Exception {
        String jsonContentRequest = "{\"from\":\"uuid1\",\"members\":[\"uuid1\",\"uuid2\",\"uuid3\",\"uuid4\"]}";

        addAchievementsIsOk(jsonContentRequest, ACHIEVEMENTS_ADD_TEAM_URL);
        addAchievementsFailed(jsonContentRequest, ACHIEVEMENTS_ADD_TEAM_URL);
    }


    private void addAchievementsIsOk(String jsonContentRequest, String urlTemplate) throws Exception {
        mockMvc.perform(post(urlTemplate)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    private void addAchievementsFailed(String jsonContentRequest, String urlTemplate) throws Exception {
        mockMvc.perform(post(urlTemplate)
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}