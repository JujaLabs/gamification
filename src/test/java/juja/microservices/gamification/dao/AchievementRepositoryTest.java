package juja.microservices.gamification.dao;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.integration.BaseIntegrationTest;
import juja.microservices.gamification.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    public void shouldAddNewAchievementAndReturnNotNullId() {
        Achievement achievement = new Achievement("sasha", "ira", 1,
                "good work", AchievementType.DAILY);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(currentDate);
        String actualId = achievementRepository.addAchievement(achievement);
        List<Achievement> achievements = achievementRepository.getAllAchievementsByUserToId("ira");

        String lineSeparator = System.lineSeparator();
        String shouldMuchRetrievedAchievement =
                "Achievement:".concat(lineSeparator)
                        .concat("id = ").concat(actualId).concat(lineSeparator)
                        .concat("from = ").concat("sasha").concat(lineSeparator)
                        .concat("to = ").concat("ira").concat(lineSeparator)
                        .concat("sendDate = ").concat(date).concat(lineSeparator)
                        .concat("point = ").concat("1").concat(lineSeparator)
                        .concat("description = ").concat("good work").concat(lineSeparator)
                        .concat("type = ").concat("DAILY").concat(lineSeparator);

        assertThat(actualId, notNullValue());
        assertEquals(shouldMuchRetrievedAchievement, achievements.get(0).toString());
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

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.APRIL,1);

        testAchievement.setSendDate(currentDate);
        testAchievementAnotherDate.setSendDate(calendar.getTime());

        String id = achievementRepository.addAchievement(testAchievement);
        achievementRepository.addAchievement(testAchievementAnotherDate);
        achievementRepository.addAchievement(testAchievementNotADaily);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(currentDate);

        String lineSeparator = System.lineSeparator();
        String shouldMuchRetrievedAchievement =
                "Achievement:".concat(lineSeparator)
                        .concat("id = ").concat(id).concat(lineSeparator)
                        .concat("from = ").concat("oleg").concat(lineSeparator)
                        .concat("to = ").concat("oleg").concat(lineSeparator)
                        .concat("sendDate = ").concat(date).concat(lineSeparator)
                        .concat("point = ").concat("1").concat(lineSeparator)
                        .concat("description = ").concat("Old daily report").concat(lineSeparator)
                        .concat("type = ").concat("DAILY").concat(lineSeparator);

        //when
        List<Achievement> list = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType("oleg",
                AchievementType.DAILY);

        //then
        assertEquals(1, list.size());
        assertEquals(shouldMuchRetrievedAchievement, list.get(0).toString());
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void getAllThanksKeepersAchievementsCurrentWeekTest(){
        Achievement achievement =
                new Achievement("sasha", "ira", 2, "keeper thanks", AchievementType.THANKS_KEEPER);
        achievement.setSendDate(getFistDayOfCurrentWeek());
        achievementRepository.addAchievement(achievement);

        List<Achievement> list = achievementRepository.getAllThanksKeepersAchievementsCurrentWeek();

        assertEquals(1, list.size());
    }

    private Date getFistDayOfCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        return calendar.getTime();
    }

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    public void getAllCodenjoyAchievementsCurrentDateTest(){
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

        List<Achievement> list = achievementRepository.getAllCodenjoyAchievementsCurrentDate();

        assertEquals(3, list.size());
    }
}