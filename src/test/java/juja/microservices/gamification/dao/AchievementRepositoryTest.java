package juja.microservices.gamification.dao;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserPointsSum;
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
    @ShouldMatchDataSet(location = "/datasets/addNewAchievement.json")
    public void shouldAddNewAchievementAndReturnNotNullId() {
        Achievement testAchievement = new Achievement("sasha", "ira", 2,
                "good work", AchievementType.DAILY);

        String actualId = achievementRepository.addAchievement(testAchievement);
        assertThat(actualId, notNullValue());
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
    public void getAllAchievementsByUserIdTest() {
        List<Achievement> list = achievementRepository.getAllAchievementsByUserToId("ira");
        assertEquals(2, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getAllUsersIdsTest() {
        List<String> list = achievementRepository.getAllUserToIDs();
        assertEquals(2, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getUserAchievementsDetailsTestForAllUsersList() {
        List<UserAchievementDetails> list = achievementRepository.getUserAchievementsDetails();
        assertEquals(2, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementByUserFromIdSendDateType.json")
    public void getAllAchievementsByUserFromIdSendDateTypeTest(){
        List<Achievement> list = achievementRepository.getAllAchievementsByUserFromIdSendDateType("oleg","2017-02-05",AchievementType.DAILY);
        String lineSeparator = System.lineSeparator();

        String shouldMuchRetrievedAchievement =
                "Achievement:".concat(lineSeparator)
                .concat("id = ").concat("579b16a4f3d8e118d0ddb2c3").concat(lineSeparator)
                .concat("userFromId = ").concat("oleg").concat(lineSeparator)
                .concat("userToId = ").concat("oleg").concat(lineSeparator)
                .concat("sendDate = ").concat("2017-02-05").concat(lineSeparator)
                .concat("pointCount = ").concat("1").concat(lineSeparator)
                .concat("description = ").concat("Old daily report").concat(lineSeparator)
                .concat("type = ").concat("DAILY").concat(lineSeparator);

        assertEquals(1,list.size());
        assertEquals(shouldMuchRetrievedAchievement,list.get(0).toString());
    }
}
