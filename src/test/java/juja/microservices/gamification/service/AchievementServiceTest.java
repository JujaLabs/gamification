package juja.microservices.gamification.service;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.entity.InterviewRequest;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

        achievementService.addDaily(userToId, shouldMuchDescription);
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

        achievementService.addDaily(userToId, updateForDescription);
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

        achievementService.addDaily(userToId, updateForDescription);
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
        achievementService.addDaily(userFrom, firstDescription);
        achievementService.addDaily(userFrom, secondDescription);
        achievementService.addDaily(userFrom, thirdDescription);

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

        achievementService.addThanks(expectedUserFrom, expectedUserTo, expectedDescription);
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
    public void throwExceptionTodayCodenjoyExist() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("You cannot give codenjoy points twice a day");
        String userFrom = "max";
        String firstUserTo = "john";
        String secondUserTo = "bob";
        String thirdUserTo = "alex";
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        achievementService.addCodenjoy(request);
        Assert.fail();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void throwExceptionEmptyFromUser() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("User from cannot be empty");
        String userFrom = "";
        String firstUserTo = "max";
        String secondUserTo = "bob";
        String thirdUserTo = "alex";
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        Assert.fail();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void throwExceptionEmptyFirstUser() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("First user cannot be empty");
        String userFrom = "max";
        String firstUserTo = "";
        String secondUserTo = "bob";
        String thirdUserTo = "alex";
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        Assert.fail();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void throwExceptionEmptySecondUserThirdUserExist() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("Second user cannot be empty");
        String userFrom = "max";
        String firstUserTo = "john";
        String secondUserTo = "";
        String thirdUserTo = "alex";
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        Assert.fail();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void throwExceptionFirstAndSecondUsersEquals() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("First and second users must be different");
        String userFrom = "max";
        String firstUserTo = "john";
        String secondUserTo = "john";
        String thirdUserTo = "alex";
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        Assert.fail();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void throwExceptionFirstAndThirdUsersEquals() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("First and third users must be different");
        String userFrom = "max";
        String firstUserTo = "john";
        String secondUserTo = "alex";
        String thirdUserTo = "john";
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        Assert.fail();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void throwExceptionSecondAndThirdUsersEquals() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("Second and third users must be different");
        String userFrom = "max";
        String firstUserTo = "alex";
        String secondUserTo = "john";
        String thirdUserTo = "john";
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        Assert.fail();
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

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldUpdateDescriptionFieldInterviewAchievement() {
        String userToId = "sasha";
        String userFromId = userToId;
        String firstDescription = "This is an interview report";
        String updateForDescription = "Some update for the interview report";
        String shouldMuchDescription = firstDescription
                .concat(System.lineSeparator())
                .concat(updateForDescription);

        InterviewRequest request = new InterviewRequest(userToId, updateForDescription);

        Achievement testAchievement = new Achievement(userFromId, userToId, 10, firstDescription, AchievementType.INTERVIEW);
        achievementRepository.addAchievement(testAchievement);

        achievementService.addInterview(request);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("sasha");
        String actualDescription = achievementList.get(0).getDescription();

        assertEquals(shouldMuchDescription, actualDescription);
    }
}
