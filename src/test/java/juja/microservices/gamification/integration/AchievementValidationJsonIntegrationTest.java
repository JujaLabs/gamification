package juja.microservices.gamification.integration;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Garazd
 */
@RunWith(SpringRunner.class)
public class AchievementValidationJsonIntegrationTest extends BaseIntegrationTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAchievementInEmptyDatabaseShouldReturnValidJson() throws Exception {
        String expectedJson =
            "[{\"userToId\":\"ira\",\"pointCount\":5}]";
        String jsonContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\"," +
                "\"pointCount\":5,\"description\":\"good work\"}";

        addAchievement(jsonContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void addAchievementInDatabaseWhereinExistingAchievementShouldReturnValidJson() throws Exception {
        String expectedJson =
            "[{\"userToId\":\"sasha\",\"pointCount\":5}," +
                "{\"userToId\":\"ira\",\"pointCount\":3}]";
        String jsonContentRequest =
            "{\"userFromId\":\"ira\",\"userToId\":\"sasha\"," +
                "\"pointCount\":4,\"description\":\"good work\"}";

        addAchievement(jsonContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void addNewAchievementInDatabaseWhereinExistingAchievementShouldReturnValidJson() throws Exception {
        String expectedJson =
            "[{\"userToId\":\"peter\",\"pointCount\":6}," +
                "{\"userToId\":\"sasha\",\"pointCount\":3}," +
                "{\"userToId\":\"max\",\"pointCount\":4}]";
        String jsonContentRequest =
            "{\"userFromId\":\"ira\",\"userToId\":\"sasha\"," +
                "\"pointCount\":3,\"description\":\"good work\"}";

        addAchievement(jsonContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test(expected = Error.class)
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void checkErrorAddInvalidAchievementInEmptyDatabase() throws Exception {
        String jsonInvalidContentRequest =
            "{\"userId\":,\"userId\":\"ira\",\"pointCount\":5,:\"good work\"}";

        addAchievement(jsonInvalidContentRequest);
    }

    @Test(expected = Error.class)
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void checkErrorAddAchievementInEmptyDatabaseCompareInvalidJson() throws Exception {
        String expectedInvalidJson =
            "[{\"userToId\":,Count\":6},{\"userToId\":},{\"userToId\":max}]";
        String jsonContentRequest =
            "{\"userFromId\":\"ira\",\"userToId\":\"sasha\"," +
                "\"pointCount\":3,\"description\":\"good work\"}";

        addAchievement(jsonContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedInvalidJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addNullAchievementInEmptyDatabaseShouldReturnValidJson() throws Exception {
        String expectedJson =
            "[{\"userToId\":\"ira\",\"pointCount\":0}]";
        String jsonContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\"," +
                "\"pointCount\":0,\"description\":\"bad work\"}";

        addAchievement(jsonContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addMinusAchievementInEmptyDatabaseShouldReturnValidJson() throws Exception {
        String expectedJson =
            "[{\"userToId\":\"ira\",\"pointCount\":-15}]";
        String jsonContentRequest =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\"," +
                "\"pointCount\":-15,\"description\":\"bad work\"}";

        addAchievement(jsonContentRequest);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void addAndMinusAchievementInDatabaseWhereinExistingAchievementShouldReturnValidJson() throws Exception {
        String expectedJson =
            "[{\"userToId\":\"peter\",\"pointCount\":5}," +
                "{\"userToId\":\"max\",\"pointCount\":4}," +
                "{\"userToId\":\"ira\",\"pointCount\":2}]";
        String jsonContentRequestAddAchievement =
            "{\"userFromId\":\"sasha\",\"userToId\":\"ira\"," +
                "\"pointCount\":2,\"description\":\"good work\"}, " +
                "\"userFromId\":\"sasha\",\"userToId\":\"peter\"," +
                "\"pointCount\":2,\"description\":\"good work\"}," +
                "\"userFromId\\\":\"sasha\",\"userToId\":\"max\",\" +\n" +
                "\"pointCount\":2,\"description\":\"good work\"}";
        String jsonContentRequestMinusAchievement =
            "{\"userFromId\":\"sasha\",\"userToId\":\"peter\"," +
                "\"pointCount\":-1,\"description\":\"error enrollment\"}";

        addAchievement(jsonContentRequestAddAchievement);

        addAchievement(jsonContentRequestMinusAchievement);

        MvcResult result = getMvcResultUserAchieveSum();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    private void addAchievement(String jsonContentRequest) throws Exception {
        mockMvc.perform(post("/achieve")
            .contentType(APPLICATION_JSON_UTF8)
            .content(jsonContentRequest))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    private MvcResult getMvcResultUserAchieveSum() throws Exception {
        return mockMvc
            .perform(MockMvcRequestBuilders.get("/user/achieveSum").contentType(APPLICATION_JSON_UTF8))
            .andReturn();
    }
}
