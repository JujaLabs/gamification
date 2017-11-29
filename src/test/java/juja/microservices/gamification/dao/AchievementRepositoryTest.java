package juja.microservices.gamification.dao;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.dao.impl.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;
import juja.microservices.integration.BaseIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Danil Kuznetsov
 */
@RunWith(SpringRunner.class)
public class AchievementRepositoryTest extends BaseIntegrationTest {

    @Inject
    private AchievementRepository achievementRepository;

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void shouldAddNewAchievementAndReturnNotNullId() {
        Achievement achievement = new Achievement("sasha", "ira", 1,
                "good work", AchievementType.DAILY);

        String actualId = achievementRepository.addAchievement(achievement);
        List<Achievement> achievements = achievementRepository.getAllAchievementsByUserToId("ira");

        assertThat(actualId, notNullValue());
        assertThat(achievements, contains(achievement));
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void shouldReturnSizeAllUsersWithPointSum() {
        List<UserPointsSum> list = achievementRepository.getAllUsersWithPointSum();
        assertEquals(2, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json")
    public void shouldReturnAllUsersNameAndPointSum() {
        List<UserPointsSum> list = achievementRepository.getAllUsersWithPointSum();

        String expectedFirstName = "max";
        int expectedSumPointUserMax = 4;
        assertEquals(expectedFirstName, list.get(0).getTo());
        assertEquals(expectedSumPointUserMax, list.get(0).getPoint());

        String expectedSecondName = "peter";
        int expectedSumPointUserPeter = 6;
        assertEquals(expectedSecondName, list.get(1).getTo());
        assertEquals(expectedSumPointUserPeter, list.get(1).getPoint());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getAllAchievementsByUserIdTest() {
        List<Achievement> list = achievementRepository.getAllAchievementsByUserToId("ira");
        assertEquals(2, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void getUserAchievementsDetailsTestForAllUsersList() {
        List<String> ids = new ArrayList<>();
        ids.add("sasha");
        ids.add("ira");
        UserIdsRequest request = new UserIdsRequest(ids);

        List<UserAchievementDetails> list = achievementRepository.getUserAchievementsDetails(request);

        assertEquals(2, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void getAllAchievementsByUserFromIdCurrentDateTypeTest() {

        Achievement testAchievement =
                new Achievement("oleg", "oleg", 1, "Old daily report", AchievementType.DAILY);
        Achievement testAchievementAnotherDate =
                new Achievement("oleg", "oleg", 1, "Old another date daily report", AchievementType.DAILY);
        Achievement testAchievementNotADaily =
                new Achievement("oleg", "olga", 1, "Not a daily report", AchievementType.THANKS);

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime anotherDate = LocalDateTime.of(2017, Month.APRIL, 1, 12, 0);

        testAchievement.setSendDate(currentDate);
        testAchievementAnotherDate.setSendDate(anotherDate);

        achievementRepository.addAchievement(testAchievement);
        achievementRepository.addAchievement(testAchievementAnotherDate);
        achievementRepository.addAchievement(testAchievementNotADaily);
        List<Achievement> achievements = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType("oleg",
                AchievementType.DAILY);

        assertEquals(1, achievements.size());
        assertThat(achievements, contains(testAchievement));
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void getAllThanksKeepersAchievementsCurrentWeekTest() {
        Achievement achievement =
                new Achievement("sasha", "ira", 2, "keeper thanks", AchievementType.THANKS_KEEPER);
        achievement.setSendDate(getDateOfMondayOfCurrentWeek());
        achievementRepository.addAchievement(achievement);
        List<Achievement> achievements = achievementRepository.getAllThanksKeepersAchievementsCurrentWeek();

        assertEquals(1, achievements.size());
        assertThat(achievements, contains(achievement));
    }

    private LocalDateTime getDateOfMondayOfCurrentWeek() {
        return LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void getAllCodenjoyAchievementsCurrentDateTest() {
        String userFromId = "sasha";
        String userFirstPlace = "ira";
        String userSecondPlace = "max";
        String userThirdPlace = "ben";

        Achievement firstPlace = new Achievement(userFromId, userFirstPlace, 3,
                "Codenjoy first place", AchievementType.CODENJOY);
        Achievement secondPlace = new Achievement(userFromId, userSecondPlace, 2,
                "Codenjoy second place", AchievementType.CODENJOY);
        Achievement thirdPlace = new Achievement(userFromId, userThirdPlace, 1,
                "Codenjoy third place", AchievementType.CODENJOY);

        achievementRepository.addAchievement(firstPlace);
        achievementRepository.addAchievement(secondPlace);
        achievementRepository.addAchievement(thirdPlace);
        List<Achievement> achievements = achievementRepository.getAllCodenjoyAchievementsCurrentDate();

        assertEquals(3, achievements.size());
        assertThat(achievements, contains(firstPlace, secondPlace, thirdPlace));
    }

    @Test
    @UsingDataSet(locations = "/datasets/addOldTeamAchievement.json")
    public void getAllTeamAchievementsCurrentWeekNoAchievements() {
        Set<String> uuids = new HashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4"));

        List<Achievement> list = achievementRepository.getAllTeamAchievementsCurrentWeek(uuids);

        assertTrue(list.isEmpty());
    }

    @Test
    @UsingDataSet(locations = "/datasets/addOldTeamAchievement.json")
    public void getAllTeamAchievementsCurrentWeekExistAchievements() {
        List<Achievement> expected = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Achievement achievement = new Achievement("uuidFrom", "uuid" + i, 6,
                    "Team activity", AchievementType.TEAM);
            expected.add(achievement);
            achievementRepository.addAchievement(achievement);
        }

        Set<String> uuids = new HashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4"));
        List<Achievement> actual = achievementRepository.getAllTeamAchievementsCurrentWeek(uuids);

        assertThat(actual, is(expected));
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void getEmptyWelcomeAchievementsByUserTest() {
        List<Achievement> list = achievementRepository.getWelcomeAchievementByUser("max");

        assertEquals(0, list.size());
    }

    @Test
    @UsingDataSet(locations = "/datasets/addWelcomeAchievement.json")
    public void getWelcomeAchievementsByUserTest() {
        List<Achievement> list = achievementRepository.getWelcomeAchievementByUser("max");

        assertEquals(1, list.size());
    }
}