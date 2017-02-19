package juja.microservices.gamification.service;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import java.util.List;
import javax.inject.Inject;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

/**
 * @author BaHo
 */
@RunWith(SpringRunner.class)
public class AchievementServiceTest extends BaseIntegrationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldUpdateDescriptionAddThreeDailyAchievement() {
        String userFrom = "sasha";
        String firstDescription = "Daily report first";
        String secondDescription = "Daily report second";
        String thirdDescription = "Daily report third";
        achievementService.addDaily(firstDescription, userFrom);
        achievementService.addDaily(secondDescription, userFrom);
        achievementService.addDaily(thirdDescription, userFrom);

        String expectedDescription = "Daily report first\r\nDaily report second\r\nDaily report third";
        String expectedType = "DAILY";
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("sasha");
        String actualDescription = achievementList.get(0).getDescription();
        String actualType = String.valueOf(achievementList.get(0).getType());

        Assert.assertTrue(achievementList.size() == 1);
        assertEquals(expectedDescription, actualDescription);
        assertEquals(expectedType, actualType);
    }

    @Test
    @UsingDataSet(locations = "/datasets/addThanksAchievement.json")
    public void shouldAddNewThanksAchievement() {
        String expectedUserFrom = "sasha";
        String expectedUserTo = "max";
        String expectedDescription = "For helping with...";
        String expectedType = "THANKS";

        achievementService.addThanks(expectedUserFrom, expectedUserTo, expectedDescription);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("max");
        String actualUserFrom = achievementList.get(0).getUserFromId();
        String actualUserTo = achievementList.get(0).getUserToId();
        String actualDescription = achievementList.get(0).getDescription();
        String actualType = String.valueOf(achievementList.get(0).getType());

        assertEquals(expectedUserFrom, actualUserFrom);
        assertEquals(expectedUserTo, actualUserTo);
        assertEquals(expectedDescription, actualDescription);
        assertEquals(expectedType, actualType);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldCorrectAddThirdThanksAchievement() {
        String userFrom = "sasha";
        String firstUserTo = "peter";
        String secondUserTo = "max";
        String description = "For helping with...";
        achievementService.addThanks(userFrom, firstUserTo, description);
        achievementService.addThanks(userFrom, secondUserTo, description);

        String expectedDescription = "Issued two thanks";
        String expectedType = "THANKS";
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("sasha");
        String actualDescription = achievementList.get(0).getDescription();
        String actualType = String.valueOf(achievementList.get(0).getType());

        assertEquals(expectedDescription, actualDescription);
        assertEquals(expectedType, actualType);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldThrowExceptionAddThanksAchievementYourself() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("You cannot thank yourself");
        String userFrom = "sasha";
        String userTo = "sasha";
        String description = "For helping with...";
        achievementService.addThanks(userFrom, userTo, description);
        Assert.fail();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldThrowExceptionAddTwoThanksAchievementOnePerson() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("You cannot give more than one thanks for day one person");
        String userFrom = "sasha";
        String userTo = "max";
        String description = "For helping with...";
        achievementService.addThanks(userFrom, userTo, description);
        achievementService.addThanks(userFrom, userTo, description);
        Assert.fail();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldThrowExceptionAddMoreThanTwoThanksAchievement() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("You cannot give more than two thanks for day");
        String userFrom = "sasha";
        String firstUserTo = "ira";
        String secondUserTo = "max";
        String thirdUserTo = "jon";
        String description = "For helping with...";
        achievementService.addThanks(userFrom, firstUserTo, description);
        achievementService.addThanks(userFrom, secondUserTo, description);
        achievementService.addThanks(userFrom, thirdUserTo, description);
        Assert.fail();
    }
}
