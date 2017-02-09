package juja.microservices.gamification.service;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import java.util.List;
import javax.inject.Inject;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

/**
 * @author BaHo
 */
@RunWith(SpringRunner.class)
public class AchievementServiceTest extends BaseIntegrationTest {

    @Inject
    private AchievementRepository achievementRepository;

    @Inject
    private AchievementService achievementService;

    @Test
    @UsingDataSet(locations = "/datasets/addDailyAchievement.json")
    public void shouldAddNewDailyAchievement() {
        String shouldMuchDescription = "This is a daily report";
        String userToId = "oleg";

        achievementService.addDaily(shouldMuchDescription, userToId);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("oleg");
        String actualDescription = achievementList.get(0).getDescription();

        assertEquals(shouldMuchDescription, actualDescription);
    }

    @Test
    @UsingDataSet(locations = "/datasets/addDailyAchievement.json")
    public void shouldUpdateExistingDailyAchievement() {
        String userToId = "oleg";
        String userFromId = userToId;
        String firstDescription = "This is a daily report";
        String updateForDescription = "Some update for the daily report";
        String shouldMuchDescription = firstDescription
            .concat(System.lineSeparator())
            .concat(updateForDescription);

        Achievement testAchievement = new Achievement(userFromId, userToId, 1, firstDescription, AchievementType.DAILY);
        achievementRepository.addAchievement(testAchievement);

        achievementService.addDaily(updateForDescription, userToId);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("oleg");
        String actualDescription = achievementList.get(0).getDescription();

        assertEquals(shouldMuchDescription, actualDescription);
    }

    @Test
    @UsingDataSet(locations = "/datasets/addDailyAchievement.json")
    public void shouldUpdateOnlyDescriptionFieldDailyAchievement() {
        String userToId = "oleg";
        String userFromId = userToId;
        String firstDescription = "This is a daily report";
        String updateForDescription = "Some update for the daily report";
        String shouldMuchDescription = firstDescription
            .concat(System.lineSeparator())
            .concat(updateForDescription);


        Achievement testAchievement = new Achievement(userFromId, userToId, 1, firstDescription, AchievementType.DAILY);
        achievementRepository.addAchievement(testAchievement);

        List<Achievement> achievementListBeforeUpdate = achievementRepository.getAllAchievementsByUserToId("oleg");
        Achievement shouldMuchAchievement = achievementListBeforeUpdate.get(0);
        shouldMuchAchievement.setDescription(shouldMuchDescription);
        String shouldMuchAchievementToString = shouldMuchAchievement.toString();

        achievementService.addDaily(updateForDescription, userToId);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("oleg");

        String updatedAchievement = achievementList.get(0).toString();

        assertEquals(shouldMuchAchievementToString, updatedAchievement);
    }

    @Test
    @UsingDataSet(locations = "/datasets/addThanksAchievement.json")
    public void shouldAddThanksAchievement() {
        String userFromId = "sasha";
        String userToId = "max";
        String expectedDescription = "For helping with...";

        achievementService.addThanks(userFromId, userToId, expectedDescription);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("max");
        String actualDescription = achievementList.get(0).getDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    @UsingDataSet(locations = "/datasets/addThanksAchievement.json")
    public void shouldExistingThirdThanksAchievement() {
        String userFromId = "sasha";
        String userToId = "sasha";
        String expectedDescription = "Issued two thanks";

        Achievement testAchievement = new Achievement(userFromId, userToId, 1, expectedDescription, AchievementType.THANKS);
        achievementRepository.addAchievement(testAchievement);

        achievementService.addThanks(userFromId, userToId, expectedDescription);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("sasha");
        String actualDescription = achievementList.get(0).getDescription();

        assertEquals(expectedDescription, actualDescription);
    }
}
