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
            "[{\"userToId\":\"ira\",\"pointCount\":1}]";
        String jsonContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\",\"description\":\"good work\"}";

        addThanksIsOk(jsonContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksShouldReturnExceptionCannotThankYourself() throws Exception {
        String jsonContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"sasha\",\"description\":\"thanks\"}";

        addThanksFailed(jsonContentRequest);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addThanksShouldReturnExceptionOneThanksOnePersonInDay() throws Exception {
        String firstContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\",\"description\":\"thanks\"}";
        String secondContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\",\"description\":\"thanks\"}";

        addThanksIsOk(firstContentRequest);
        addThanksFailed(secondContentRequest);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addNewThanksShouldReturnExceptionNotMoreTwoThanks() throws Exception {
        String firstContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\",\"description\":\"thanks\"}";
        String secondContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"max\",\"description\":\"thanks\"}";
        String thirdContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"peter\",\"description\":\"thanks\"}";

        addThanksIsOk(firstContentRequest);
        addThanksIsOk(secondContentRequest);
        addThanksFailed(thirdContentRequest);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addNewTwoThanksShouldReturnOneForMe() throws Exception {
        String expectedJson =
            "[{\"userToId\":\"sasha\",\"pointCount\":1}," +
                "{\"userToId\":\"max\",\"pointCount\":1}," +
                "{\"userToId\":\"ira\",\"pointCount\":1}]";
        String firstContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\",\"description\":\"thanks\"}";
        String secondContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"max\",\"description\":\"thanks\"}";

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

    private MvcResult getMvcResultUserAchieveSum() throws Exception {
        return mockMvc
            .perform(MockMvcRequestBuilders.get("/user/pointSum").contentType(APPLICATION_JSON_UTF8))
            .andReturn();
    }
}
