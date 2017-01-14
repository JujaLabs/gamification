package juja.microservices.gamification;

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
public class IntegrationTestValidationJson extends BaseIntegrationTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addAchievementIsEmptyDatabaseShouldReturnValidJson() throws Exception {
        String expectedJson = "[{\"userToId\":\"ira\",\"pointCount\":5}]";
        String jsonContentRequest = "{\"userFromId\":\"sasha\",\"userToId\":\"ira\"," +
            "\"pointCount\":5,\"description\":\"good work\"}";

        mockMvc.perform(post("/achieve")
            .contentType(APPLICATION_JSON_UTF8)
            .content(jsonContentRequest))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.get("/user/achieveSum").contentType(APPLICATION_JSON_UTF8))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void addAchievementIsDatabaseExistingAchievementShouldReturnValidJson() throws Exception {
        String expectedJson = "[{\"userToId\":\"sasha\",\"pointCount\":6},{\"userToId\":\"ira\",\"pointCount\":3}]";
        String jsonContentRequest = "{\"userFromId\":\"ira\",\"userToId\":\"sasha\"," +
            "\"pointCount\":5,\"description\":\"good work\"}";

        mockMvc.perform(post("/achieve")
            .contentType(APPLICATION_JSON_UTF8)
            .content(jsonContentRequest))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.get("/user/achieveSum").contentType(APPLICATION_JSON_UTF8))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void addNewAchievementIsDatabaseExistingAchievementShouldAndReturnValidJson() throws Exception {
        String expectedJson = "[{\"userToId\":\"peter\",\"pointCount\":6},{\"userToId\":\"sasha\",\"pointCount\":5},{\"userToId\":\"max\",\"pointCount\":4}]";
        String jsonContentRequest = "{\"userFromId\":\"ira\",\"userToId\":\"sasha\"," +
            "\"pointCount\":5,\"description\":\"good work\"}";

        mockMvc.perform(post("/achieve")
            .contentType(APPLICATION_JSON_UTF8)
            .content(jsonContentRequest))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.get("/user/achieveSum").contentType(APPLICATION_JSON_UTF8))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(expectedJson, content);
    }
}
