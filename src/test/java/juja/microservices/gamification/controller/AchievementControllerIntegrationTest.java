package juja.microservices.gamification.controller;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.regex.Pattern;
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

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createAchievementShouldCheckReturnedJson() throws Exception {

        String jsonContentRequest = "{\"userFromId\":\"sasha\",\"userToId\":\"ira\"," +
                "\"pointCount\":2,\"description\":\"good work\"}";

        String regExIdValidator = "\\{\"id\":\"[0-9a-zA-Z]{24}\"\\}";

        String idJsonAsString = mockMvc.perform(post("/achieve")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(jsonContentRequest))
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

        Assert.assertTrue(Pattern.matches(regExIdValidator,idJsonAsString));
    }
    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    @ShouldMatchDataSet(location = "/datasets/addNewAchievement-expected.json")
    public void createAchievementShouldAddNewAchievementAndReturnJson() throws Exception {
        String jsonContentRequest = "{\"userFromId\":\"sasha\",\"userToId\":\"ira\"," +
                "\"pointCount\":2,\"description\":\"good work\"}";

        mockMvc.perform(post("/achieve")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void getAllUsersWithAchievementAndReturnJson() throws Exception {
        mockMvc.perform(get("/user/achieveSum")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getUsersAchievementDetailsAndReturnJson()throws Exception{
        mockMvc.perform(get("/user/achieveDetails")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
}
