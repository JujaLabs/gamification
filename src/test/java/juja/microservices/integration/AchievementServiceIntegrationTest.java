package juja.microservices.integration;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.dao.impl.AchievementRepository;
import juja.microservices.gamification.dao.KeeperRepository;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.exceptions.WelcomeAchievementException;
import juja.microservices.gamification.service.AchievementService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * @author BaHo
 * @author Vadim Dyachenko
 */
@RunWith(SpringRunner.class)
public class AchievementServiceIntegrationTest extends BaseIntegrationTest {

    @Value("${system.from.uuid}")
    private String systemFrom;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    private AchievementRepository achievementRepository;

    @MockBean
    private KeeperRepository keeperRepository;

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

        String expectedDescription = "You got 'thanks' achievement for thanking to other two users";
        String expectedType = "THANKS";
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId(userFrom);
        String actualDescription = achievementList.get(0).getDescription();
        String actualType = String.valueOf(achievementList.get(0).getType());

        assertEquals(expectedDescription, actualDescription);
        assertEquals(expectedType, actualType);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void addCodenjoy() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String userFrom = "max";
        String firstUserTo = "john";
        String secondUserTo = "bob";
        String thirdUserTo = "alex";
        String firstDescription = "Codenjoy first place";
        String secondDescription = "Codenjoy second place";
        String thirdDescription = "Codenjoy third place";
        String expectedType = "CODENJOY";
        String expectedDate = dateFormat.format(new Date(System.currentTimeMillis()));
        CodenjoyRequest request = new CodenjoyRequest(userFrom, firstUserTo, secondUserTo, thirdUserTo);
        achievementService.addCodenjoy(request);
        List<Achievement> achievementList = achievementRepository.getAllCodenjoyAchievementsCurrentDate();
        Assert.assertTrue(achievementList.size() == 3);
        achievementList.forEach(achievement -> {
            assertEquals(userFrom, achievement.getFrom());
            assertEquals(expectedType, achievement.getType().toString());
            assertEquals(expectedDate, dateFormat.format(achievement.getSendDate()));
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

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldAddWelcomeAchievement() {
        String userFromId = "max";
        String userToId = "john";
        int expectedPoints = 1;
        AchievementType expectedType = AchievementType.WELCOME;
        String expectedDescription = "Welcome to JuJa!";
        WelcomeRequest request = new WelcomeRequest(userFromId, userToId);

        achievementService.addWelcome(request);
        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("john");
        Achievement achievement = achievementList.get(0);
        String actualFromId = achievement.getFrom();
        String actualToId = achievement.getTo();
        int actualPoints = achievement.getPoint();
        AchievementType actualType = achievement.getType();
        String actualDescription = achievement.getDescription();

        assertEquals(userFromId, actualFromId);
        assertEquals(userToId, actualToId);
        assertEquals(expectedPoints, actualPoints);
        assertEquals(expectedType, actualType);
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldAddThanksKeeperAchievement() {
        //given
        String expectedUuid = "0002A";
        int expectedPoints = 2;
        AchievementType expectedType = AchievementType.THANKS_KEEPER;
        String expectedDescription = "Thank you for keeping in the direction of Codenjoy";

        List<String> directions = Arrays.asList("Codenjoy");
        KeeperDTO keeper = new KeeperDTO("0002A", directions);
        List<KeeperDTO> keepers = Arrays.asList(keeper);
        when(keeperRepository.getKeepers()).thenReturn(keepers);

        //when
        achievementService.addThanksKeeper();

        List<Achievement> achievementList = achievementRepository.getAllAchievementsByUserToId("0002A");
        Achievement achievement = achievementList.get(0);
        String actualFrom = achievement.getFrom();
        String actualUuid = achievement.getTo();
        int actualPoints = achievement.getPoint();
        AchievementType actualType = achievement.getType();
        String actualDescription = achievement.getDescription();

        //Then
        assertEquals(systemFrom, actualFrom);
        assertEquals(expectedUuid, actualUuid);
        assertEquals(expectedPoints, actualPoints);
        assertEquals(expectedType, actualType);
        assertEquals(expectedDescription, actualDescription);
    }

    @Test(expected = WelcomeAchievementException.class)
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldAddTwoWelcomeAchievement() {
        String userFromId = "max";
        String userToId = "john";
        WelcomeRequest request = new WelcomeRequest(userFromId, userToId);

        achievementService.addWelcome(request);
        achievementService.addWelcome(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldAddTeamAchievements() {
        //given
        int teamPoints = 6;
        int teamSize = 4;
        String userFromID = "uuid1";
        String firstUserToID = "uuid1";
        String secondUserToID = "uuid2";
        String thirdUserToID = "uuid3";
        String fourthUserToID = "uuid4";
        String description = "Work in team";
        String expectedType = "TEAM";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expectedDate = dateFormat.format(new Date(System.currentTimeMillis()));
        Set<String> expectedMembers = new HashSet<>(
                Arrays.asList(firstUserToID, secondUserToID, thirdUserToID, fourthUserToID));
        TeamRequest request = new TeamRequest(userFromID, expectedMembers);

        //when
        achievementService.addTeam(request);
        List<Achievement> achievementList = achievementRepository.getAllTeamAchievementsCurrentWeek(expectedMembers);

        //then
        Assert.assertTrue(achievementList.size() == teamSize);
        Set<String> actualMembers = new HashSet<>();
        achievementList.forEach(achievement -> {
            actualMembers.add(achievement.getTo());
            assertEquals(userFromID, achievement.getFrom());
            assertEquals(expectedType, achievement.getType().toString());
            assertEquals(expectedDate, dateFormat.format(achievement.getSendDate()));
            int point = achievement.getPoint();
            String actualDescription = achievement.getDescription();
            if (point == teamPoints) {
                assertEquals(description, actualDescription);
            } else {
                fail("Incorrect number of points");
            }
        });
        assertEquals(teamSize, actualMembers.size());
    }
}