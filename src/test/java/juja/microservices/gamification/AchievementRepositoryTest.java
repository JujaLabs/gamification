package juja.microservices.gamification;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserPointsSum;
import juja.microservices.gamification.integration.BaseIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

/**
 * @author danil.kuznetsov
 */
@RunWith(SpringRunner.class)
public class AchievementRepositoryTest extends BaseIntegrationTest {

    @Inject
    private AchievementRepository achievementRepository;

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    @ShouldMatchDataSet(location = "/datasets/addNewAchievement-expected.json")
    public void shouldAddNewAchievementAndReturnNotNullId() {
        Achievement testAchievement = new Achievement("sasha", "ira", 2, "good work", AchievementType.DAILY);

        String actualId = achievementRepository.addAchievement(testAchievement);
        assertThat(actualId,notNullValue());
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void shouldReturnSizeAllUsersWithAchievements() {
        List<UserPointsSum> list = achievementRepository.getAllUsersWithAchievement();
        assertEquals(2, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void shouldReturnAllUsersNameAndSumAchievements() {
        List<UserPointsSum> list = achievementRepository.getAllUsersWithAchievement();

        String expectedFirstName = "peter";
        int expectedSumPointUserPeter = 6;
        assertEquals(expectedFirstName, list.get(0).getUserToId());
        assertEquals(expectedSumPointUserPeter, list.get(0).getPointCount());

        String expectedSecondName = "max";
        int expectedSumPointUserMax = 4;
        assertEquals(expectedSecondName, list.get(1).getUserToId());
        assertEquals(expectedSumPointUserMax, list.get(1).getPointCount());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getAllAchievementsByUserIdTest(){
        List<Achievement> list = achievementRepository.getAllAchievementsByUserId("ira");
        assertEquals(2,list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getAllUsersIdsTest(){
       List <String> list = achievementRepository.getAllUserToIDs();
       assertEquals(2, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getUserAchievementsDetailsTestForAllUsersList(){
        List<UserAchievementDetails> list = achievementRepository.getUserAchievementsDetails();
        assertEquals(2,list.size());
    }
}
