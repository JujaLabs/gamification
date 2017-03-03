package juja.microservices.gamification.controller;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author danil.kuznetsov
 */
@RunWith(SpringRunner.class)
public class AchievementControllerIntegrationTest extends BaseIntegrationTest {

    private MockMvc mockMvc;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void getAllUsersWithAchievementAndReturnJson() throws Exception {
        mockMvc.perform(get("/user/pointSum")
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getUsersAchievementDetailsAndReturnJson() throws Exception {
        String json = "{\"toIds\":[\"sasha\", \"ira\"]}";
        mockMvc.perform(post("/user/achieveDetails")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksShouldReturnValidJson() throws Exception {
        String expectedJson =
            "[{\"to\":\"ira\",\"point\":1}]";
        String jsonContentRequest =
            "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"good work\"}";

        addThanksIsOk(jsonContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksShouldReturnExceptionCannotThankYourself() throws Exception {
        String jsonContentRequest =
            "{\"from\":\"sasha\",\"to\":\"sasha\",\"description\":\"thanks\"}";

        addThanksFailed(jsonContentRequest);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksShouldReturnExceptionOneThanksOnePersonInDay() throws Exception {
        String firstContentRequest =
            "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";
        String secondContentRequest =
            "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";

        addThanksIsOk(firstContentRequest);
        addThanksFailed(secondContentRequest);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addNewThanksShouldReturnExceptionNotMoreTwoThanks() throws Exception {
        String firstContentRequest =
            "{\"from\":\"sasha\",\"to\":\"ira\",\"description\":\"thanks\"}";
        String secondContentRequest =
            "{\"from\":\"sasha\",\"to\":\"max\",\"description\":\"thanks\"}";
        String thirdContentRequest =
            "{\"from\":\"sasha\",\"to\":\"peter\",\"description\":\"thanks\"}";

        addThanksIsOk(firstContentRequest);
        addThanksIsOk(secondContentRequest);
        addThanksFailed(thirdContentRequest);
    }

    //TODO This test can't be accurate because an order of objects in array of result can be unpredictable
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

        addThanksIsOk(firstContentRequest);
        addThanksIsOk(secondContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    private void addThanksIsOk(String jsonContentRequest) throws Exception {
        mockMvc.perform(post("/achieve/thanks")
            .contentType(APPLICATION_JSON_UTF8)
            .content(jsonContentRequest))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    private void addThanksFailed(String jsonContentRequest) throws Exception {
        mockMvc.perform(post("/achieve/thanks")
            .contentType(APPLICATION_JSON_UTF8)
            .content(jsonContentRequest))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    //TODO fix order of objects in expectedJson
    public void addCodenjoyReturnValidJson() throws Exception {
        String jsonContentRequest =
                "{\"from\":\"max\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";
        String expectedJson =
                "[{\"to\":\"tomas\",\"point\":1},{\"to\":\"alex\",\"point\":5},{\"to\":\"jack\",\"point\":3}]";
        addCodenjoyIsOk(jsonContentRequest);
        MvcResult result = getMvcResultUserAchieveSum();
        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addCodenjoyShouldReturnExceptionFromMissed() throws Exception {
        String jsonContentRequest =
                "{\"from\":\"\",\"firstPlace\":\"alex\",\"secondPlace\":\"jack\",\"thirdPlace\":\"tomas\"}";
        addCodenjoyFailed(jsonContentRequest);
    }

    private void addCodenjoyIsOk(String jsonContentRequest) throws Exception {
        mockMvc.perform(post("/achieve/codenjoy")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    private void addCodenjoyFailed(String jsonContentRequest) throws Exception {
        mockMvc.perform(post("/achieve/codenjoy")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    private MvcResult getMvcResultUserAchieveSum() throws Exception {
        return mockMvc
            .perform(MockMvcRequestBuilders.get("/user/pointSum").contentType(APPLICATION_JSON_UTF8))
            .andReturn();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addInterviewReturnValidJson() throws Exception {
        String jsonContentRequest = "{\"from\":\"sasha\",\"description\":\"interview report\"}";
        String expectedJson = "[{\"to\":\"sasha\",\"point\":10}]";

        mockMvc.perform(post("/achieve/interview")
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

        mockMvc.perform(post("/achieve/interview")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}
