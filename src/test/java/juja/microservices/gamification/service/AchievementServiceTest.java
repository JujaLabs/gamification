package juja.microservices.gamification.service;

import juja.microservices.WithoutScheduling;
import juja.microservices.gamification.dao.impl.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.entity.DailyRequest;
import juja.microservices.gamification.entity.InterviewRequest;
import juja.microservices.gamification.entity.KeeperDTO;
import juja.microservices.gamification.entity.TeamRequest;
import juja.microservices.gamification.entity.ThanksRequest;
import juja.microservices.gamification.entity.WelcomeRequest;
import juja.microservices.gamification.exceptions.CodenjoyAchievementException;
import juja.microservices.gamification.exceptions.CodenjoyAchievementTwiceInOneDayException;
import juja.microservices.gamification.exceptions.TeamAchievementException;
import juja.microservices.gamification.exceptions.ThanksAchievementMoreThanOneException;
import juja.microservices.gamification.exceptions.ThanksAchievementMoreThanTwoException;
import juja.microservices.gamification.exceptions.ThanksAchievementTryToThanksYourselfException;
import juja.microservices.gamification.exceptions.WelcomeAchievementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(AchievementService.class)
public class AchievementServiceTest implements WithoutScheduling {

    private static final String FIRST_ACHIEVEMENT_ID = "1";
    private static final String SECOND_ACHIEVEMENT_ID = "2";
    private static final String THIRD_ACHIEVEMENT_ID = "3";
    private static final String FOURTH_ACHIEVEMENT_ID = "4";
    private static final int CODENJOY_FIRST_PLACE = 5;
    private static final int CODENJOY_SECOND_PLACE = 3;
    private static final int CODENJOY_THIRD_PLACE = 1;
    private static final int ONE_POINT = 1;
    private static final int THANKS_KEEPER = 2;
    private static final int WELCOME_POINTS = 1;
    private static final String WELCOME_DESCRIPTION = "Welcome to JuJa!";
    private static final int TEAM_POINTS = 6;

    @Inject
    private AchievementService service;

    @MockBean
    private AchievementRepository repository;
    @MockBean
    private KeeperService keeperService;

    @Test
    public void addDaily() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        List<String> expectedList = Arrays.asList(FIRST_ACHIEVEMENT_ID);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.DAILY))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        DailyRequest request = new DailyRequest("max", "Daily");

        List<String> actualList = service.addDaily(request);

        assertEquals(expectedList, actualList);
        ArgumentCaptor<Achievement> captor = ArgumentCaptor.forClass(Achievement.class);
        verify(repository).getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.DAILY);
        verify(repository).addAchievement(captor.capture());
        assertEquals("max", captor.getValue().getFrom());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void addSecondDaily() throws Exception {
        Achievement achievement = new Achievement("max", "max", ONE_POINT, "Daily",
                AchievementType.DAILY);
        List<Achievement> userFromIdList = Arrays.asList(achievement);
        List<String> expectedList = Arrays.asList(FIRST_ACHIEVEMENT_ID);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.DAILY))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        DailyRequest request = new DailyRequest("max", "Second daily");

        List<String> actualList = service.addDaily(request);

        assertEquals(expectedList, actualList);
        ArgumentCaptor<Achievement> captor = ArgumentCaptor.forClass(Achievement.class);
        verify(repository).getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.DAILY);
        verify(repository).addAchievement(captor.capture());
        assertEquals("max", captor.getValue().getFrom());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void addThanks() throws Exception {
        List<Achievement> userFromIdList = new ArrayList<>();
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        ThanksRequest request = new ThanksRequest("max", "john", "Thanks");
        List<String> expectedList = Arrays.asList(FIRST_ACHIEVEMENT_ID);

        List<String> actualList = service.addThanks(request);

        assertEquals(expectedList, actualList);
        ArgumentCaptor<Achievement> captor = ArgumentCaptor.forClass(Achievement.class);
        verify(repository).getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS);
        verify(repository).addAchievement(captor.capture());
        assertEquals("max", captor.getValue().getFrom());
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = ThanksAchievementTryToThanksYourselfException.class)
    public void addThanksToYourself() throws Exception {
        ThanksRequest request = new ThanksRequest("max", "max", "Thanks");

        service.addThanks(request);

        fail();
    }

    @Test(expected = ThanksAchievementMoreThanOneException.class)
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
        verify(repository).getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void addSecondThanks() throws Exception {
        Achievement achievement = new Achievement("max", "john", ONE_POINT, "Thanks",
                AchievementType.THANKS);
        List<Achievement> userFromIdList = Arrays.asList(achievement);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(SECOND_ACHIEVEMENT_ID)
                .thenReturn(THIRD_ACHIEVEMENT_ID);

        List<String> expectedList = Arrays.asList(SECOND_ACHIEVEMENT_ID, THIRD_ACHIEVEMENT_ID);

        ThanksRequest request = new ThanksRequest("max", "bill", "Second thanks");
        List<String> actualList = service.addThanks(request);

        assertEquals(expectedList, actualList);
        verify(repository).getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS);
        verify(repository, times(2)).addAchievement(any(Achievement.class));
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = ThanksAchievementMoreThanTwoException.class)
    public void addThirdThanks() throws Exception {
        Achievement firstAchievement = new Achievement("max", "john", ONE_POINT, "Thanks",
                AchievementType.THANKS);
        Achievement secondAchievement = new Achievement("max", "bob", ONE_POINT, "Thanks",
                AchievementType.THANKS);
        List<Achievement> userFromIdList = Arrays.asList(firstAchievement, secondAchievement);
        when(repository.getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS))
                .thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenThrow(ThanksAchievementMoreThanTwoException.class);
        ThanksRequest request = new ThanksRequest("max", "bill", "Third thanks");

        service.addThanks(request);

        fail();
        verify(repository).getAllAchievementsByUserFromIdCurrentDateType("max", AchievementType.THANKS);
        verify(repository).addAchievement(any(Achievement.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void addCodenjoy() throws Exception {
        List<String> expectedList = Arrays.asList(FIRST_ACHIEVEMENT_ID, SECOND_ACHIEVEMENT_ID, THIRD_ACHIEVEMENT_ID);

        List<Achievement> userFromIdList = new ArrayList<>();
        when(repository.getAllCodenjoyAchievementsCurrentDate()).thenReturn(userFromIdList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID).
                thenReturn(SECOND_ACHIEVEMENT_ID).thenReturn(THIRD_ACHIEVEMENT_ID);
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "bob");

        List<String> actualList = service.addCodenjoy(request);

        assertEquals(expectedList, actualList);
        verify(repository).getAllCodenjoyAchievementsCurrentDate();
        verify(repository, times(3)).addAchievement(any(Achievement.class));
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = CodenjoyAchievementTwiceInOneDayException.class)
    public void addSecondCodenjoy() throws Exception {
        Achievement firstAchievement = new Achievement("max", "john", CODENJOY_FIRST_PLACE, "First place codenjoy",
                AchievementType.CODENJOY);
        Achievement secondAchievement = new Achievement("max", "bill", CODENJOY_SECOND_PLACE, "Second place codenjoy",
                AchievementType.CODENJOY);
        Achievement thirdAchievement = new Achievement("max", "bob", CODENJOY_THIRD_PLACE, "Third place codenjoy",
                AchievementType.CODENJOY);
        List<Achievement> userFromIdList = Arrays.asList(firstAchievement, secondAchievement, thirdAchievement);

        when(repository.getAllCodenjoyAchievementsCurrentDate()).thenReturn(userFromIdList);
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "bob");

        service.addCodenjoy(request);

        fail();
        verify(repository).getAllCodenjoyAchievementsCurrentDate();
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = CodenjoyAchievementException.class)
    public void addCodenjoyWithSameFirstAndSecondPlaces() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "john", "bill");

        service.addCodenjoy(request);

        fail();
    }

    @Test(expected = CodenjoyAchievementException.class)
    public void addCodenjoyWithSameFirstAndThirdPlaces() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "john");

        service.addCodenjoy(request);

        fail();
    }

    @Test(expected = CodenjoyAchievementException.class)
    public void addCodenjoyWithSameSecondAndThirdPlaces() throws Exception {
        CodenjoyRequest request = new CodenjoyRequest("max", "john", "bill", "bill");

        service.addCodenjoy(request);

        fail();
    }

    @Test
    public void addInterview() throws Exception {
        List<String> expectedList = Arrays.asList(FIRST_ACHIEVEMENT_ID);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);
        InterviewRequest request = new InterviewRequest("max", "Interview");

        List<String> actualList = service.addInterview(request);

        assertEquals(expectedList, actualList);
        verify(repository).addAchievement(any(Achievement.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void addKeeperThanks() throws Exception {
        List<String> expectedList = Arrays.asList(FIRST_ACHIEVEMENT_ID, SECOND_ACHIEVEMENT_ID, THIRD_ACHIEVEMENT_ID);
        List<String> firstKeeperDirections = Arrays.asList("First direction", "Second direction");
        List<String> secondKeeperDirections = Arrays.asList("Third direction");

        List<KeeperDTO> keepersList = new ArrayList<>();
        keepersList.add(new KeeperDTO("0002A", firstKeeperDirections));
        keepersList.add(new KeeperDTO("0002B", secondKeeperDirections));

        when(keeperService.getKeepers()).thenReturn(keepersList);
        when(repository.addAchievement(any(Achievement.class)))
                .thenReturn(FIRST_ACHIEVEMENT_ID).thenReturn(SECOND_ACHIEVEMENT_ID).thenReturn(THIRD_ACHIEVEMENT_ID);

        List<String> actualList = service.addThanksKeeper();

        assertEquals(expectedList, actualList);
        verify(keeperService).getKeepers();
        verify(repository).getAllThanksKeepersAchievementsCurrentWeek();
        verify(repository, times(3)).addAchievement(any(Achievement.class));
        verifyNoMoreInteractions(repository, keeperService);
    }

    @Test
    public void addKeeperThanksTwicePerCurrentWeek() throws Exception {
        Achievement achievement = new Achievement(
                "alex", "0002A", THANKS_KEEPER, "good job", AchievementType.THANKS_KEEPER);
        List<Achievement> achievements = Arrays.asList(achievement);
        String expectedId = achievements.get(0).getId();
        when(repository.getAllThanksKeepersAchievementsCurrentWeek()).thenReturn(achievements);

        List<String> actualList = service.addThanksKeeper();

        assertEquals(expectedId, actualList.get(0));
        verify(repository).getAllThanksKeepersAchievementsCurrentWeek();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void addKeeperThanksWithoutActiveKeeperTest() throws Exception {
        when(keeperService.getKeepers()).thenReturn(new ArrayList<>());
        when(repository.getAllThanksKeepersAchievementsCurrentWeek()).thenReturn(new ArrayList<>());

        List<String> actualList = service.addThanksKeeper();

        assertTrue(actualList.isEmpty());
        verify(keeperService).getKeepers();
        verify(repository).getAllThanksKeepersAchievementsCurrentWeek();
        verifyNoMoreInteractions(repository, keeperService);
    }

    @Test
    public void addWelcome() throws Exception {
        List<Achievement> welcomeList = new ArrayList<>();
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FIRST_ACHIEVEMENT_ID);
        WelcomeRequest request = new WelcomeRequest("max", "john");
        when(repository.getWelcomeAchievementByUser("john")).thenReturn(welcomeList);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID);

        List<String> actualList = service.addWelcome(request);

        assertEquals(expectedList, actualList);
        verify(repository).getWelcomeAchievementByUser("john");
        verify(repository).addAchievement(any(Achievement.class));
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = WelcomeAchievementException.class)
    public void addSecondWelcome() throws Exception {
        Achievement achievement = new Achievement("max", "john", WELCOME_POINTS, WELCOME_DESCRIPTION,
                AchievementType.WELCOME);
        List<Achievement> welcomeList = Arrays.asList(achievement);
        WelcomeRequest request = new WelcomeRequest("max", "john");
        when(repository.getWelcomeAchievementByUser("john")).thenReturn(welcomeList);

        service.addWelcome(request);

        fail();
        verify(repository).getWelcomeAchievementByUser("john");
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void addTeamAchievements() throws Exception {
        List<String> expectedList = new ArrayList<>(Arrays.asList(FIRST_ACHIEVEMENT_ID, SECOND_ACHIEVEMENT_ID,
                THIRD_ACHIEVEMENT_ID, FOURTH_ACHIEVEMENT_ID));
        Set<String> members = new HashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4"));
        TeamRequest request = new TeamRequest("uuid1", members);
        when(repository.addAchievement(any(Achievement.class))).thenReturn(FIRST_ACHIEVEMENT_ID).
                thenReturn(SECOND_ACHIEVEMENT_ID).thenReturn(THIRD_ACHIEVEMENT_ID).thenReturn(FOURTH_ACHIEVEMENT_ID);
        when(repository.getAllTeamAchievementsCurrentWeek(eq(members))).thenReturn(new ArrayList<>());

        List<String> actualList = service.addTeam(request);

        assertEquals(expectedList, actualList);
        verify(repository, times(4)).addAchievement(any(Achievement.class));
        verify(repository).getAllTeamAchievementsCurrentWeek(eq(members));
        verifyNoMoreInteractions(repository);
    }

    @Test(expected = TeamAchievementException.class)
    public void addTeamAchievementsWhenMembersHaveTeamPointsThisWeek() throws Exception {
        Set<String> members = new HashSet<>(Arrays.asList("uuid1", "uuid2", "uuid3", "uuid4"));
        TeamRequest request = new TeamRequest("uuid1", members);
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement(
                "uuid1", "uuid2", TEAM_POINTS, "team work", AchievementType.TEAM));
        when(repository.getAllTeamAchievementsCurrentWeek(eq(members))).thenReturn(achievements);

        service.addTeam(request);

        fail();
        verify(repository).getAllTeamAchievementsCurrentWeek(eq(members));
        verifyNoMoreInteractions(repository);
    }
}