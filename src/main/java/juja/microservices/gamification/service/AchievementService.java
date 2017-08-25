package juja.microservices.gamification.service;

import java.util.ArrayList;
import javax.inject.Inject;

import juja.microservices.gamification.dao.impl.AchievementRepository;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AchievementService {

    private static final int DAILY_POINTS = 1;
    private static final int TWO_THANKS = 2;
    private static final int THANKS_POINTS = 1;
    private static final int INTERVIEW_POINTS = 10;
    private static final int CODENJOY_FIRST_PLACE = 5;
    private static final int CODENJOY_SECOND_PLACE = 3;
    private static final int CODENJOY_THIRD_PLACE = 1;
    private static final int KEEPER_THANKS = 2;
    private static final int WELCOME_POINTS = 1;
    private static final String WELCOME_DESCRIPTION = "Welcome to JuJa!";
    private static final String SYSTEM_FROM = "JuJa";
    private static final String THANKS_DESCRIPTION = "Thank you for keeping in the direction of %s";
    private static final int TEAM_POINTS = 6;
    private static final String TEAM_DESCRIPTION = "Work in team";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private AchievementRepository achievementRepository;
    @Inject
    private KeeperService keeperService;
    @Inject
    private TeamService teamService;

    /**
     * In this method userFromId = userToId because users add DAILY achievements to themselves.
     * If the DAILY achievement already exists in the database and user wants to add another DAILY
     * achievement at the same day, the only field description will be updated.
     */
    public List<String> addDaily(DailyRequest request) {
        String userFromId = request.getFrom();
        String description = request.getDescription();
        logger.debug("Send request to repository: get daily achievement by user <{}> for current date", userFromId);
        List<Achievement> userFromIdList = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType(
                userFromId, AchievementType.DAILY);
        List<String> result = new ArrayList<>();
        Achievement achievement;
        if (userFromIdList.size() == 0) {
            logger.debug("Received empty data from repository");
            achievement = new Achievement(userFromId, userFromId, DAILY_POINTS, description, AchievementType.DAILY);
        } else {
            logger.debug("Received achievement id from repository: [{}]", userFromIdList.get(0).getId());
            achievement = userFromIdList.get(0);
            String oldDescription = achievement.getDescription();
            description = oldDescription
                    .concat(System.lineSeparator())
                    .concat(description);
            achievement.setDescription(description);
        }
        logger.debug("Send 'Daily' achievement to repository");
        result.add(achievementRepository.addAchievement(achievement));
        logger.debug("Received id from repository: {}", result);

        return result;
    }

    public List<String> addThanks(ThanksRequest request) {
        String fromId = request.getFrom();
        String toId = request.getTo();
        String description = request.getDescription();

        if (fromId.equalsIgnoreCase(toId)) {
            logger.warn("User '{}' tried to put 'Thanks' achievement to yourself", request.getTo());
            throw new ThanksAchievementTryToThanksYourselfException("User tried to put 'Thanks' achievement to yourself");
        }
        logger.debug("Send request to repository: get thanks achievements by user <{}> for current date", fromId);
        List<Achievement> userFromThanksAchievementToday = achievementRepository
                .getAllAchievementsByUserFromIdCurrentDateType(fromId, AchievementType.THANKS);
        logger.debug("Received list of achievements, size = {}", userFromThanksAchievementToday.size());
        if (userFromThanksAchievementToday.size() >= TWO_THANKS) {
            logger.warn("User '{}' tried to give 'Thanks' achievement more than two times per day", fromId);
            throw new ThanksAchievementMoreThanTwoException(
                    "User tried to give 'Thanks' achievement more than two times per day");
        }

        for (Achievement achievement : userFromThanksAchievementToday) {
            if (achievement.getTo().equals(toId)) {
                logger.warn("User '{}' tried to give 'Thanks' achievement more than one times to person '{}'", fromId,
                        toId);
                throw new ThanksAchievementMoreThanOneException(
                        "User tried to give 'Thanks' achievement more than one times to person");
            }
        }

        List<String> result = new ArrayList<>();
        Achievement achievement = new Achievement(fromId, toId, THANKS_POINTS, description, AchievementType.THANKS);
        logger.debug("Send 'Thanks' achievement to repository");
        result.add(achievementRepository.addAchievement(achievement));

        if (!userFromThanksAchievementToday.isEmpty()) {
            String descriptionTwoThanks = ("You got 'thanks' achievement for thanking to other two users");

            Achievement achievementTwoThanks = new Achievement(fromId, fromId, THANKS_POINTS, descriptionTwoThanks,
                    AchievementType.THANKS);
            logger.debug("Send additional 'Thanks' achievement to repository");
            result.add(achievementRepository.addAchievement(achievementTwoThanks));
        }

        logger.debug("Received id from repository: {}", result);
        return result;
    }

    public List<String> addCodenjoy(CodenjoyRequest request) {
        checkUsers(request);
        String userFromId = request.getFrom();
        logger.debug("Send request to repository: get all codenjoy achievements for current date");
        List<Achievement> codenjoyUsersToday = achievementRepository.getAllCodenjoyAchievementsCurrentDate();
        logger.debug("Received list of achievements, size = {}", codenjoyUsersToday.size());
        if (!codenjoyUsersToday.isEmpty()) {
            logger.warn("User '{}' tried to give 'Codenjoy' achievement points twice a day", userFromId);
            throw new CodenjoyAchievementTwiceInOneDayException("User tried to give 'Codenjoy' achievement points twice a day");
        }

        List<String> result = addCodenjoyAchievement(request);
        logger.debug("Received id from repository: {}", result);
        return result;
    }

    private void checkUsers(CodenjoyRequest request) {
        logger.debug("Verification users in codenjoy request");
        String firstUserId = request.getFirstPlace();
        String secondUserId = request.getSecondPlace();
        String thirdUserId = request.getThirdPlace();

        if (firstUserId.equalsIgnoreCase(secondUserId)) {
            logger.warn("Codenjoy request rejected: first and second place users is same");
            throw new CodenjoyAchievementException("First and second users is same");
        }
        if (firstUserId.equalsIgnoreCase(thirdUserId)) {
            logger.warn("Codenjoy request rejected: first and third place users is same");
            throw new CodenjoyAchievementException("First and third users is same");
        }
        if (secondUserId.equalsIgnoreCase(thirdUserId)) {
            logger.warn("Codenjoy request rejected: second and third place users is same");
            throw new CodenjoyAchievementException("Second and third users is same");
        }
        logger.debug("Verification was successful");
    }

    private List<String> addCodenjoyAchievement(CodenjoyRequest request) {
        logger.debug("Preparing achievement for send to repository");
        String userFromId = request.getFrom();
        String userFirstPlace = request.getFirstPlace();
        String userSecondPlace = request.getSecondPlace();
        String userThirdPlace = request.getThirdPlace();

        Achievement firstPlace = new Achievement(userFromId, userFirstPlace, CODENJOY_FIRST_PLACE,
                "Codenjoy first place", AchievementType.CODENJOY);
        Achievement secondPlace = new Achievement(userFromId, userSecondPlace, CODENJOY_SECOND_PLACE,
                "Codenjoy second place", AchievementType.CODENJOY);
        Achievement thirdPlace = new Achievement(userFromId, userThirdPlace, CODENJOY_THIRD_PLACE,
                "Codenjoy third place", AchievementType.CODENJOY);

        List<String> result = new ArrayList<>();
        result.add(achievementRepository.addAchievement(firstPlace));
        logger.debug("Add fist place 'Codenjoy' achievement from user '{}' to '{}'", userFromId, userFirstPlace);
        result.add(achievementRepository.addAchievement(secondPlace));
        logger.debug("Add second place 'Codenjoy' achievement from user '{}' to '{}'", userFromId, userSecondPlace);
        result.add(achievementRepository.addAchievement(thirdPlace));
        logger.debug("Add third place 'Codenjoy' achievement from user '{}' to '{}'", userFromId, userThirdPlace);

        return result;
    }

    public List<String> addInterview(InterviewRequest request) {

        String userFromId = request.getFrom();
        String description = request.getDescription();
        Achievement newAchievement = new Achievement(userFromId, userFromId, INTERVIEW_POINTS, description,
                AchievementType.INTERVIEW);
        List<String> result = new ArrayList<>();
        logger.debug("Send 'Interview' achievement to repository");
        result.add(achievementRepository.addAchievement(newAchievement));
        logger.debug("Received id from repository: {}", result);
        return result;
    }

    @Scheduled(cron = "${cron.expression}")
    private void addScheduledThanksKeeper() {
        if (getThanksKeeperAchievements().isEmpty()) {
            List<String> achievementIds = createThanksKeeperAchievements();
            logger.info("Added Thanks Keeper achievement from scheduled task. Ids: {}", achievementIds);
        }
    }

    public List<String> addThanksKeeper() {
        List<Achievement> achievements = getThanksKeeperAchievements();
        return achievements.isEmpty() ? createThanksKeeperAchievements() : getIdsCreatedAchievement(achievements);
    }

    private List<Achievement> getThanksKeeperAchievements() {
        logger.debug("Send request to repository: get all thanks_keepers achievements for current week");
        List<Achievement> achievements = achievementRepository.getAllThanksKeepersAchievementsCurrentWeek();
        logger.debug("Received list of achievements, size = {}", achievements.size());
        return achievements;
    }

    private List<String> createThanksKeeperAchievements() {
        List<String> result = new ArrayList<>();
        logger.debug("Request to keepers repository: get all active keepers");
        List<KeeperDTO> keepers = keeperService.getKeepers();
        logger.debug("Received list of active keepers, size = {} ", keepers.size());
        if (keepers.isEmpty()) {
            logger.debug("No any active keepers received from user service");
        } else {
            logger.debug("Sending 'Thanks Keeper' achievements to repository");
            for (KeeperDTO keeper : keepers) {
                List<String> directions = keeper.getDirections();
                directions.forEach(direction -> {
                    String description = String.format(THANKS_DESCRIPTION, direction);
                    Achievement achievement = new Achievement(
                            SYSTEM_FROM, keeper.getUuid(), KEEPER_THANKS, description, AchievementType.THANKS_KEEPER);
                    result.add(achievementRepository.addAchievement(achievement));
                });
            }
            logger.debug("Received ids from repository: {}", result);
        }
        return result;
    }

    private List<String> getIdsCreatedAchievement(List<Achievement> achievements) {
        List<String> result = getIds(achievements);
        logger.info("Returned already created 'Thanks Keeper' achievements in current week {}", result.toString());

        return result;
    }

    private List<String> getIds(List<Achievement> achievements) {
        List<String> result = new ArrayList<>();
        achievements.forEach(achievement -> result.add(achievement.getId()));
        return result;
    }

    public List<String> addWelcome(WelcomeRequest request) {

        String userFromId = request.getFrom();
        String userToId = request.getTo();

        logger.debug("Send request to repository: get welcome achievement by user <{}>", userToId);
        List<Achievement> welcome = achievementRepository.getWelcomeAchievementByUser(userToId);

        if (!welcome.isEmpty()) {
            logger.warn("User '{}' tried to give 'Welcome' achievement more than one time to {}",
                    userFromId, userToId);
            throw new WelcomeAchievementException(
                    "User tried to give 'Welcome' achievement more than one time to one person");
        } else {
            List<String> result = new ArrayList<>();
            Achievement newAchievement = new Achievement(userFromId, userToId, WELCOME_POINTS, WELCOME_DESCRIPTION,
                    AchievementType.WELCOME);
            logger.debug("Send new 'Welcome' achievement to repository");
            result.add(achievementRepository.addAchievement(newAchievement));
            logger.debug("Received id from repository: {}", result);

            return result;
        }
    }

    public List<String> addTeam(String uuid) {
        logger.debug("Preparing team achievements for send to repository");
        TeamDTO team = teamService.getTeamByUuid(uuid);
        Set<String> members = team.getMembers();
        List<Achievement> teamAchievements = achievementRepository.getAllTeamAchievementsCurrentWeek(members);
        if (!teamAchievements.isEmpty() ) {
            logger.warn("User '{}' tried to give 'Team' achievements but some members have such achievements this week",
                    uuid);
            throw new TeamAchievementException("Cannot add 'Team' achievements. Some team members have such " +
                    " achievements this week.");
        }
        List<String> result = new ArrayList<>();
        members.forEach(userUuid -> {
            result.add(achievementRepository.addAchievement(
                    new Achievement(uuid, userUuid, TEAM_POINTS,TEAM_DESCRIPTION, AchievementType.TEAM))
            );
            logger.debug("Add 'Team' achievement from user '{}' to '{}'", uuid, userUuid);
        });
        return result;
    }
}