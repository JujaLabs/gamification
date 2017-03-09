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
        Achievement achievement = new Achievement("max", "max", 1, "Daily",
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
        List<String> ids = service.addThanks(request);
        List<String> thanks = new ArrayList<>();
        thanks.add(FIRST_ACHIEVEMENT_ID);
        assertEquals(thanks, ids);
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
        Achievement achievement = new Achievement("max", "john", 1, "Thanks",
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
        Achievement achievement = new Achievement("max", "john", 1, "Thanks",
                AchievementType.THANKS);
        userFromIdList.add(achievement);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(SECOND_ACHIEVEMENT_ID)
                .thenReturn(THIRD_ACHIEVEMENT_ID);
        List<String> thanks = new ArrayList<>();
        thanks.add(SECOND_ACHIEVEMENT_ID);
        thanks.add(THIRD_ACHIEVEMENT_ID);
        ThanksRequest request = new ThanksRequest("max", "bill","Second thanks");
        List<String> ids = service.addThanks(request);
        assertEquals(thanks, ids);
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void addThirdThanks() throws Exception {
        Achievement firstAchievement = new Achievement("max", "john", 1, "Thanks",
                AchievementType.THANKS );
        Achievement secondAchievement = new Achievement("max", "bob", 1, "Thanks",
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
        List<String> ids = service.addCodenjoy(request);
        List<String> codenjoy = new ArrayList<>();
        codenjoy.add(FIRST_ACHIEVEMENT_ID);
        codenjoy.add(SECOND_ACHIEVEMENT_ID);
        codenjoy.add(THIRD_ACHIEVEMENT_ID);
        assertEquals(codenjoy, ids);
    }

    @Test(expected = UnsupportedAchievementException.class)
    public void addSecondCodenjoy() throws Exception {
        Achievement firstAchievement = new Achievement("max", "john", 5, "First place codenjoy",
                AchievementType.CODENJOY );
        Achievement secondAchievement = new Achievement("max", "bill", 3, "Second place codenjoy",
                AchievementType.CODENJOY );
        Achievement thirdAchievement = new Achievement("max", "bob", 1, "Third place codenjoy",
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