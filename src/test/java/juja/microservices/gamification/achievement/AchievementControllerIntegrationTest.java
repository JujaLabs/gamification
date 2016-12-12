package juja.microservices.gamification.achievement;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by danil.kuznetsov on 01/12/16.
 */
@RunWith(SpringRunner.class)
public class AchievementControllerIntegrationTest extends BaseIntegrationTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    @ShouldMatchDataSet(location = "/datasets/addNewAchievement-expected.json")
    public void sendAchievementShouldAddNewAchievementAndReturnJson() throws Exception {
        String jsonContentRequest = "{\"userFromId\":\"sasha\",\"userToId\":\"ira\"," +
                "\"pointCount\":2,\"description\":\"good work\"}";

        mockMvc.perform(post("/achieve")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonContentRequest))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
<<<<<<< HEAD

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void getListAllUsersWithAchievementAndReturnJson() throws Exception {

        mockMvc.perform(get("/achieve")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
=======
    @Test
    @UsingDataSet(locations = "/datasets/selectAchieventById.json")
    public void sendUsersShouldSendAllUserAchievementDetails()throws Exception{
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
>>>>>>> gameorigin/achievement
    }
}
