package juja.microservices.integration;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.service.AchievementService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author BaHo
 */
@RunWith(SpringRunner.class)
public class AchievementServiceIntegrationTest extends BaseIntegrationTest {

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

        DailyRequest request = new DailyRequest(userToId, shouldMuchDescription);

        achievementService.addDaily(request);
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

        DailyRequest request = new DailyRequest(userToId, updateForDescription);

        Achievement testAchievement = new Achievement(userFromId, userToId, 1, firstDescription, AchievementType.DAILY);
        achievementRepository.addAchievement(testAchievement);

        achievementService.addDaily(request);
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

        DailyRequest request = new DailyRequest(userToId, updateForDescription);

        Achievement testAchievement = new Achievement(userFromId, userToId, 1, firstDescription, AchievementType.DAILY);
        achievementRepository.addAchievement(testAchievement);

        List<Achievement> achievementListBeforeUpdate = achievementRepository.getAllAchievementsByUserToId("oleg");
        Achievement shouldMuchAchievement = achievementListBeforeUpdate.get(0);
        shouldMuchAchievement.setDescription(shouldMuchDescription);
        String shouldMuchAchievementToString = shouldMuchAchievement.toString();

        achievementService.addDaily(request);
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

        DailyRequest firstRequest = new DailyRequest(userFrom, firstDescription);
        DailyRequest secondRequest = new DailyRequest(userFrom, secondDescription);
        DailyRequest thirdRequest = new DailyRequest(userFrom, thirdDescription);

        achievementService.addDaily(firstRequest);
        achievementService.addDaily(secondRequest);
        achievementService.addDaily(thirdRequest);

        String expectedDescription = firstDescription
                .concat(System.lineSeparator())
                .concat(secondDescription)
                .concat(System.lineSeparator())
                .concat(thirdDescription);
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

        ThanksRequest request = new ThanksRequest(expectedUserFrom, expectedUserTo, expectedDescription);
        achievementService.addThanks(request);

        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("max");
        String actualUserFrom = achievementList.get(0).getFrom();
        String actualUserTo = achievementList.get(0).getTo();
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

        ThanksRequest firstRequest = new ThanksRequest(userFrom, firstUserTo, description);
        ThanksRequest secondRequest = new ThanksRequest(userFrom, secondUserTo, description);
        achievementService.addThanks(firstRequest);
        achievementService.addThanks(secondRequest);

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
    public void addCodenjoy() {
        String userFrom = "max";
        String firstUserTo = "john";
        String secondUserTo = "bob";
        String thirdUserTo = "alex";
        String firstDescription = "Codenjoy first place";
        String secondDescription = "Codenjoy second place";
        String thirdDescription = "Codenjoy third place";
        String expectedType = "CODENJOY";
        String expectedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        List<Achievement> achievementList = achievementRepository.getAllCodenjoyAchievementsCurrentDate();
        Assert.assertTrue(achievementList.size() == 3);
        achievementList.forEach(achievement -> {
            assertEquals(userFrom, achievement.getFrom());
            assertEquals(expectedType, achievement.getType().toString());
            assertEquals(expectedDate, achievement.getSendDate());
            int point = achievement.getPoint();
            String actualDescription = achievement.getDescription();
            if (point == 5) {
                assertEquals(firstUserTo, achievement.getTo());
                assertEquals(firstDescription, actualDescription);
            } else if (point == 3) {
                assertEquals(secondUserTo, achievement.getTo());
                assertEquals(secondDescription, actualDescription);
            } else if (point == 1) {
                assertEquals(thirdUserTo, achievement.getTo());
                assertEquals(thirdDescription, actualDescription);
            } else {
                fail("Incorrect number of points");
            }
        });
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldAddNewInterviewAchievement() {
        String shouldMuchDescription = "This is an interview report";
        String userToId = "sasha";
        InterviewRequest request = new InterviewRequest(userToId, shouldMuchDescription);

        achievementService.addInterview(request);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("sasha");
        String actualDescription = achievementList.get(0).getDescription();

        assertEquals(shouldMuchDescription, actualDescription);
    }
}
