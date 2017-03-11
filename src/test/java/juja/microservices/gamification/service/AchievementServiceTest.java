package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(AchievementService.class)
public class AchievementServiceTest {

    private static final String FIRST_ACHIEVEMENT_ID = "1";
    private static final String SECOND_ACHIEVEMENT_ID = "2";
    private static final String THIRD_ACHIEVEMENT_ID = "3";
    private static final int CODENJOY_FIRST_PLACE = 5;
    private static final int CODENJOY_SECOND_PLACE = 3;
    private static final int CODENJOY_THIRD_PLACE = 1;
    private static final int ONE_POINT = 1;

    @Inject
    private MockMvc mockMvc;

    @Inject
    private AchievementService service;

    @MockBean
    private AchievementRepository repository;

    @Test
    public void addDaily() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.DAILY))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        DailyRequest request = new DailyRequest("max", "Daily");
        String id = service.addDaily(request);
        assertEquals(FIRST_ACHIEVEMENT_ID, id);
    }

    @Test
    public void addSecondDaily() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        Achievement achievement = new Achievement("max", "max", ONE_POINT, "Daily",
                AchievementType.DAILY );
        userFromIdList.add(achievement);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.DAILY))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        DailyRequest request = new DailyRequest("max", "Second daily");
        String id = service.addDaily(request);
        assertEquals(FIRST_ACHIEVEMENT_ID, id);
    }

    @Test
    public void addThanks() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        ThanksRequest request = new ThanksRequest("max", "john", "Thanks");
        List<String> actualList = service.addThanks(request);
        List<String> expectedlist = new ArrayList<>();
        expectedlist.add(FIRST_ACHIEVEMENT_ID);
        assertEquals(expectedlist, actualList);
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void addThanksToYourself() throws Exception {
        ThanksRequest request = new ThanksRequest("max", "max", "Thanks");
        service.addThanks(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void addSecondThanksToOneUser() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        Achievement achievement = new Achievement("max", "john", ONE_POINT, "Thanks",
                AchievementType.THANKS);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        userFromIdList.add(achievement);
        ThanksRequest request = new ThanksRequest("max", "john", "Thanks");
        service.addThanks(request);
        fail();
    }

    @Test
    public void addSecondThanks() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        Achievement achievement = new Achievement("max", "john", ONE_POINT, "Thanks",
                AchievementType.THANKS);
        userFromIdList.add(achievement);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(SECOND_ACHIEVEMENT_ID)
                .thenReturn(THIRD_ACHIEVEMENT_ID);
        List<String> expectedList = new ArrayList<>();
        expectedList.add(SECOND_ACHIEVEMENT_ID);
        expectedList.add(THIRD_ACHIEVEMENT_ID);
        ThanksRequest request = new ThanksRequest("max", "bill","Second thanks");
        List<String> actualList = service.addThanks(request);
        assertEquals(expectedList, actualList);
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void addThirdThanks() throws Exception {
        Achievement firstAchievement = new Achievement("max", "john", ONE_POINT, "Thanks",
                AchievementType.THANKS );
        Achievement secondAchievement = new Achievement("max", "bob", ONE_POINT, "Thanks",
                AchievementType.THANKS );
        List<Achievement> userFromIdList = new ArrayList<>();
        userFromIdList.add(firstAchievement);
        userFromIdList.add(secondAchievement);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenThrow(UnsupportedAchievementException.class);
        ThanksRequest request = new ThanksRequest("max", "bill","Third thanks");
        service.addThanks(request);
        fail();
    }

    @Test
    public void addCodenjoy() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        when(repository.getAllCodenjoyAchievementsCurrentDate()).thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID).
                thenReturn(SECOND_ACHIEVEMENT_ID).thenReturn(THIRD_ACHIEVEMENT_ID);
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "bob");
        List<String> actualList = service.addCodenjoy(request);
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FIRST_ACHIEVEMENT_ID);
        expectedList.add(SECOND_ACHIEVEMENT_ID);
        expectedList.add(THIRD_ACHIEVEMENT_ID);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void addCodenjoyTwoPlaces() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        when(repository.getAllCodenjoyAchievementsCurrentDate()).thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID).
                thenReturn(SECOND_ACHIEVEMENT_ID).thenReturn(THIRD_ACHIEVEMENT_ID);
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "");
        List<String> actualList = service.addCodenjoy(request);
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FIRST_ACHIEVEMENT_ID);
        expectedList.add(SECOND_ACHIEVEMENT_ID);
        assertEquals(expectedList, actualList);
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void addSecondCodenjoy() throws Exception {
        Achievement firstAchievement = new Achievement("max", "john", CODENJOY_FIRST_PLACE, "First place codenjoy",
                AchievementType.CODENJOY );
        Achievement secondAchievement = new Achievement("max", "bill", CODENJOY_SECOND_PLACE, "Second place codenjoy",
                AchievementType.CODENJOY );
        Achievement thirdAchievement = new Achievement("max", "bob", CODENJOY_THIRD_PLACE, "Third place codenjoy",
                AchievementType.CODENJOY );
        List<Achievement> userFromIdList = new ArrayList<>();
        userFromIdList.add(firstAchievement);
        userFromIdList.add(secondAchievement);
        userFromIdList.add(thirdAchievement);
        when(repository.getAllCodenjoyAchievementsCurrentDate()).thenReturn(userFromIdList);
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "bob");
        service.addCodenjoy(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void CodenjoyEmptyFrom() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("", "john", "bill", "bob");
        service.addCodenjoy(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void CodenjoyEmptyFirstUserNotEmptySecondUser() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "", "bill", "bob");
        service.addCodenjoy(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void CodenjoyEmptySecondUserNotEmptyThirdUser() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "", "bob");
        service.addCodenjoy(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void CodenjoyFirstUserEqualsSecondUser() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "john", "bob");
        service.addCodenjoy(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void CodenjoyFirstUserEqualsThirdUser() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "john");
        service.addCodenjoy(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void CodenjoySecondUserEqualsThirdUser() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "bill");
        service.addCodenjoy(request);
        fail();
    }

    @Test
    public void addInterview() throws Exception {
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        InterviewRequest request = new InterviewRequest("max", "Interview");
        String id = service.addInterview(request);
        assertEquals(FIRST_ACHIEVEMENT_ID, id);
    }
}