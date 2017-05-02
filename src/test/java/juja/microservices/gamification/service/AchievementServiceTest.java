package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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
    private static final int THANKS_KEEPER = 2;


    @Inject
    private AchievementService service;

    @MockBean
    private AchievementRepository repository;
    @MockBean
    private KeeperService keeperService;

    @Test
    public void addDaily() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FIRST_ACHIEVEMENT_ID);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.DAILY))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        DailyRequest request = new DailyRequest("max", "Daily");
        List<String> actualList = service.addDaily(request);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void addSecondDaily() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        Achievement achievement = new Achievement("max", "max", ONE_POINT, "Daily",
                AchievementType.DAILY );
        userFromIdList.add(achievement);
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FIRST_ACHIEVEMENT_ID);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.DAILY))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        DailyRequest request = new DailyRequest("max", "Second daily");
        List<String> actualList = service.addDaily(request);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void addThanks() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        ThanksRequest request = new ThanksRequest("max", "john", "Thanks");
        List<String> actualList = service.addThanks(request);
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FIRST_ACHIEVEMENT_ID);
        assertEquals(expectedList, actualList);
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
    public void addCodenjoyWithSameFirstAndSecondPlaces() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "john", "bill");
        service.addCodenjoy(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void addCodenjoyWithSameFirstAndThirdPlaces() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "john");
        service.addCodenjoy(request);
        fail();
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void addCodenjoyWithSameSecondAndThirdPlaces() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "bill");
        service.addCodenjoy(request);
        fail();
    }

    @Test
    public void addInterview() throws Exception {
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FIRST_ACHIEVEMENT_ID);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        InterviewRequest request = new InterviewRequest("max", "Interview");
        List<String> actualList = service.addInterview(request);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void addKeeperThanks() throws Exception {
        //given
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FIRST_ACHIEVEMENT_ID);
        List<Keeper> keepersList = new ArrayList<>();
        keepersList.add(new Keeper("AAA", "thanks for keeper", "alex"));
        when(keeperService.getKeepers()).thenReturn(keepersList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);

        //when
        List<String> actualList = service.addThanksKeeper();

        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    public void addKeeperThanksTwicePerCurrentWeek() throws Exception {
        //given
        List<Keeper> keepersList = new ArrayList<>();
        keepersList.add(new Keeper("AAA", "job", "alex"));
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement("alex","AAA",THANKS_KEEPER, "job", AchievementType.THANKS_KEEPER));
        String expectedId = achievements.get(0).getId();

        when(keeperService.getKeepers()).thenReturn(keepersList);
        when(repository.getAllThanksKeepersAchievementsCurrentWeek()).thenReturn(achievements);

        //when
        List<String> actualList = service.addThanksKeeper();

        //then
        assertEquals(expectedId, actualList.get(0));
    }

    @Test
    public void addKeeperThanksWithoutActiveKeeperTest() throws Exception {
        //given
        when(keeperService.getKeepers()).thenReturn(new ArrayList<>());
        when(repository.getAllThanksKeepersAchievementsCurrentWeek()).thenReturn(new ArrayList<>());

        //when
        List<String> actualList = service.addThanksKeeper();

        //then
        assertTrue(actualList.isEmpty());
    }
}