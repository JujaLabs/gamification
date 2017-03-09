package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
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
        ThanksRequest request = new ThanksRequest("max", "john","Thanks");
        List<String> ids = service.addThanks(request);
        List<String> thanks = new ArrayList<>();
        thanks.add(FIRST_ACHIEVEMENT_ID);
        assertEquals(thanks, ids);
    }

    @Test
    public void addSecondThanks() throws Exception {
        int id = 2;
        List<Achievement> userFromIdList = new ArrayList<>();
        Achievement achievement = new Achievement("max", "john", 1, "Thanks",
                AchievementType.DAILY );
        userFromIdList.add(achievement);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(""+id);
//        when(repository.addAchievement(any(Achievement.class))).thenReturn(THIRD_ACHIEVEMENT_ID);
//        when(repository.addAchievement(any(Achievement.class))).thenReturn(
//                        THIRD_ACHIEVEMENT_ID
//        );

//        Achievement achievement1 = Mockito.mock(Achievement.class);
//        achievement1.
//        when(repository.addAchievement(achievement1, new Mock )).thenReturn(SECOND_ACHIEVEMENT_ID);
//
//
//        when(repository.addAchievement(any(Achievement.class))).thenAnswer(
//                invocation -> {
//                    Object[] args = invocation.getArguments();
//                    System.out.println("zzzz"+args[0]);
//                    return args[0];
//                });

//        when(repository.addAchievement(any(Achievement.class))).thenAnswer(
//                invocation -> invocation.getArgumentAt(0, String.class));


        ThanksRequest request = new ThanksRequest("max", "bill","Second thanks");
        List<String> ids = service.addThanks(request);
        List<String> thanks = new ArrayList<>();
        id = 2;
        thanks.add(SECOND_ACHIEVEMENT_ID);
        id = 3;
        thanks.add(THIRD_ACHIEVEMENT_ID);
        assertEquals(thanks, ids);
    }

    @Test
    public void addCodenjoy() throws Exception {

    }

    @Test
    public void addSecondCodenjoy() throws Exception {

    }

    @Test
    public void addInterview() throws Exception {
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        InterviewRequest request = new InterviewRequest("max", "Interview");
        String id = service.addInterview(request);
        assertEquals(FIRST_ACHIEVEMENT_ID, id);
    }
}