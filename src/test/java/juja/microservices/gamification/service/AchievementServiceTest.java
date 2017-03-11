package juja.microservices.gamification.service;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

import javax.inject.Inject;
import juja.microservices.integration.BaseIntegrationTest;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author BaHo
 */
@RunWith(SpringRunner.class)
public class AchievementServiceTest extends BaseIntegrationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    private AchievementService achievementService;

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldThrowExceptionAddThanksAchievementYourself() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("You cannot thank yourself");
        String userFrom = "sasha";
        String userTo = "sasha";
        String description = "For helping with...";

        ThanksRequest request = new ThanksRequest(userFrom, userTo, description);
        achievementService.addThanks(request);
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

        ThanksRequest request = new ThanksRequest(userFrom, userTo, description);
        achievementService.addThanks(request);
        achievementService.addThanks(request);
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

        ThanksRequest firstRequest = new ThanksRequest(userFrom, firstUserTo, description);
        ThanksRequest secondRequest = new ThanksRequest(userFrom, secondUserTo, description);
        ThanksRequest thirdRequest = new ThanksRequest(userFrom, thirdUserTo, description);

        achievementService.addThanks(firstRequest);
        achievementService.addThanks(secondRequest);
        achievementService.addThanks(thirdRequest);
        Assert.fail();
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
    public void throwExceptionDescriptionisEmpty() {
        expectedException.expect(UnsupportedAchievementException.class);
        expectedException.expectMessage("You must be enter interview report");
        String userToId = "sasha";
        String description = "";

        InterviewRequest request = new InterviewRequest(userToId, description);

        achievementService.addInterview(request);
        Assert.fail();
    }
}
