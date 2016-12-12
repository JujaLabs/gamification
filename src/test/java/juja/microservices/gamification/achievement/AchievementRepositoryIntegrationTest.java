package juja.microservices.gamification.achievement;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.achivement.Achievement;
import juja.microservices.gamification.achivement.AchievementDetail;
import juja.microservices.gamification.achivement.AchievementRepository;
import juja.microservices.gamification.achivement.UserAchievementDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

/**
 * Created by danil.kuznetsov on 01/12/16.
 */
@RunWith(SpringRunner.class)
public class AchievementRepositoryIntegrationTest extends BaseIntegrationTest {

    @Inject
    private AchievementRepository achievementRepository;

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    @ShouldMatchDataSet(location = "/datasets/addNewAchievement-expected.json")
    public void shouldAddNewAchievementAndReturnNotNullId() {
        Achievement testAchievement = new Achievement("sasha", "ira", 2, "good work");

        String actualId = achievementRepository.addAchievement(testAchievement);
        assertThat(actualId,notNullValue());
    }
    @Test
    @UsingDataSet(locations = "/datasets/selectAchieventById.json")
    public void getAllAchievementsByUserIdTest(){
        List<AchievementDetail> list = achievementRepository.getAllAchievementsByUserId("ira");
        assertEquals(2,list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchieventById.json")
    public void getUserAchievementsDetailsTest(){
        List <String> usersList = new ArrayList<>();
        usersList.add("ira");
        usersList.add("sasha");
        usersList.add("peter");
        List<UserAchievementDetails> list =
                achievementRepository.getUserAchievementsDetails(usersList);
        assertEquals(3,list.size());
    }
    @Test
    @UsingDataSet(locations = "/datasets/selectAchieventById.json")
    public void getAllUsersIdsTest(){

       List <String> list = achievementRepository.getAllUserToIDs("achievement");
       assertEquals(2,list.size());
    }
    @Test
    @UsingDataSet(locations = "/datasets/selectAchieventById.json")
    public void getUserAchievementsDetailsTestForAllUsersList(){
        List<UserAchievementDetails> list =
                achievementRepository.getUserAchievementsDetails();
        assertEquals(2,list.size());
    }
}
