package juja.microservices.gamification.service.impl;

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
import juja.microservices.gamification.service.AchievementService;
import juja.microservices.gamification.service.KeeperService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AchievementServiceImpl implements AchievementService {

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
    private static final String THANKS_DESCRIPTION = "Thank you for keeping in the direction of %s";
    private static final int TEAM_POINTS = 6;
    private static final String TEAM_DESCRIPTION = "Work in team";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${system.from.uuid}")
    private String systemFrom;

    private final AchievementRepository achievementRepository;
    private final KeeperService keeperService;

    public AchievementServiceImpl(KeeperService keeperService, AchievementRepository achievementRepository) {
        this.keeperService = keeperService;
        this.achievementRepository = achievementRepository;
    }

    /**
     * In this method userFromId = userToId because users add DAILY achievements to themselves.
     * If the DAILY achievement already exists in the database and user wants to add another DAILY
     * achievement at the same day, the only field description will be updated.
     */
    @Override
    public List<String> addDaily(DailyRequest request) {
        String userFromId = request.getFrom();
        String description = request.getDescription();
        logger.debug("Send request to repository: get already created DAILY achievement by user '{}' for current date", userFromId);
        List<Achievement> userFromIdList = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType(
                userFromId, AchievementType.DAILY);
        Achievement achievement;
        if (userFromIdList.size() == 0) {
            logger.debug("Received empty data from repository. Creating new DAILY achievement.");
            achievement = new Achievement(userFromId, userFromId, DAILY_POINTS, description, AchievementType.DAILY);
        } else {
            logger.debug("Received existing achievement. Id: [{}]", userFromIdList.get(0).getId());
            achievement = userFromIdList.get(0);
            String oldDescription = achievement.getDescription();
            description = oldDescription
                    .concat(System.lineSeparator())
                    .concat(description);
            achievement.setDescription(description);
        }
        logger.debug("Send DAILY achievement to repository");
        String id = achievementRepository.addAchievement(achievement);
        logger.info("Added DAILY achievement from user: '{}', id: [{}]", request.getFrom(), id);
        return Collections.singletonList(id);
    }

    public List<String> addThanks(ThanksRequest request) {
        String fromId = request.getFrom();
        String toId = request.getTo();
        String description = request.getDescription();

        if (fromId.equalsIgnoreCase(toId)) {
            logger.warn("User '{}' tried to put THANKS achievement to yourself", request.getTo());
            throw new ThanksAchievementTryToThanksYourselfException("User tried to put THANKS achievement to yourself");
        }
        logger.debug("Send request to repository: get already created THANKS achievements by user '{}' for current date", fromId);
        List<Achievement> userFromThanksAchievementToday = achievementRepository
                .getAllAchievementsByUserFromIdCurrentDateType(fromId, AchievementType.THANKS);
        logger.debug("Received list of THANKS achievements, size = {}", userFromThanksAchievementToday.size());
        if (userFromThanksAchievementToday.size() >= TWO_THANKS) {
            logger.warn("User '{}' tried to give THANKS achievement more than two times per day", fromId);
            throw new ThanksAchievementMoreThanTwoException(
                    "User tried to give THANKS achievement more than two times per day");
        }

        for (Achievement achievement : userFromThanksAchievementToday) {
            if (achievement.getTo().equals(toId)) {
                logger.warn("User '{}' tried to give THANKS achievement more than one times to person '{}'", fromId,
                        toId);
                throw new ThanksAchievementMoreThanOneException(
                        "User tried to give THANKS achievement more than one times to person");
            }
        }

        Achievement achievement = new Achievement(fromId, toId, THANKS_POINTS, description, AchievementType.THANKS);
        logger.debug("Send THANKS achievement to repository");
        List<String> result = new ArrayList<>();
        result.add(achievementRepository.addAchievement(achievement));

        if (!userFromThanksAchievementToday.isEmpty()) {
            String descriptionTwoThanks = ("You got THANKS achievement for thanking to other two users");

            Achievement achievementTwoThanks = new Achievement(fromId, fromId, THANKS_POINTS, descriptionTwoThanks,
                    AchievementType.THANKS);
            logger.debug("Send additional THANKS achievement to repository");
            result.add(achievementRepository.addAchievement(achievementTwoThanks));
        }

        logger.info("Added THANKS achievements from user: '{}', ids: {}", fromId, result);
        return result;
    }

    public List<String> addCodenjoy(CodenjoyRequest request) {
        checkUsers(request);
        String userFromId = request.getFrom();
        logger.debug("Send request to repository: get already created CODENJOY achievements for current date");
        List<Achievement> codenjoyUsersToday = achievementRepository.getAllCodenjoyAchievementsCurrentDate();
        logger.debug("Received list of CODENJOY achievements, size = {}", codenjoyUsersToday.size());
        if (!codenjoyUsersToday.isEmpty()) {
            logger.warn("User '{}' tried to give CODENJOY achievement points twice a day", userFromId);
            throw new CodenjoyAchievementTwiceInOneDayException("User tried to give CODENJOY achievement points twice a day");
        }

        List<String> result = addCodenjoyAchievement(request);
        logger.info("Added CODENJOY achievements, ids: {}", result);
        return result;
    }

    private void checkUsers(CodenjoyRequest request) {
        logger.debug("Verification users in codenjoy request");
        String firstUserId = request.getFirstPlace();
        String secondUserId = request.getSecondPlace();
        String thirdUserId = request.getThirdPlace();

        if (firstUserId.equalsIgnoreCase(secondUserId)) {
            logger.warn("CODENJOY request rejected: first and second place users is same");
            throw new CodenjoyAchievementException("First and second users is same");
        }
        if (firstUserId.equalsIgnoreCase(thirdUserId)) {
            logger.warn("CODENJOY request rejected: first and third place users is same");
            throw new CodenjoyAchievementException("First and third users is same");
        }
        if (secondUserId.equalsIgnoreCase(thirdUserId)) {
            logger.warn("CODENJOY request rejected: second and third place users is same");
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
        result.add(achievementRepository.addAchievement(secondPlace));
        result.add(achievementRepository.addAchievement(thirdPlace));

        return result;
    }

    public List<String> addInterview(InterviewRequest request) {
        String userFromId = request.getFrom();
        String description = request.getDescription();
        Achievement achievement = new Achievement(userFromId, userFromId, INTERVIEW_POINTS, description,
                AchievementType.INTERVIEW);

        logger.debug("Send INTERVIEW achievement to repository");
        String id = achievementRepository.addAchievement(achievement);
        logger.info("Added INTERVIEW achievement from user: '{}', id: [{}]", request.getFrom(), id);
        return Collections.singletonList(id);
    }


    @Scheduled(cron = "${keeper.thanks.cron.expression}")
    private void addThanksKeeperScheduled() {
        List<Achievement> achievements = getThanksKeeperAchievements();
        if (achievements.isEmpty()) {
            List<String> achievementIds = createThanksKeeperAchievements();
            logger.info("Added THANKS KEEPER achievement from scheduled task. Ids: {}", achievementIds);
        }
    }

    public List<String> addThanksKeeper() {
        List<Achievement> achievements = getThanksKeeperAchievements();
        List<String> achievementIds;
        if (achievements.isEmpty()) {
            achievementIds = createThanksKeeperAchievements();
            logger.info("Added new THANKS KEEPER achievement. Ids: {}", achievementIds);
        } else {
            achievementIds = achievements.stream()
                    .map(Achievement::getId)
                    .collect(Collectors.toList());
            logger.info("Returned already created THANKS KEEPER achievements in current week {}", achievementIds);
        }
        return achievementIds;
    }

    private List<Achievement> getThanksKeeperAchievements() {
        logger.debug("Send request to repository: get already created THANKS KEEPER achievements for current week");
        List<Achievement> achievements = achievementRepository.getAllThanksKeepersAchievementsCurrentWeek();
        logger.debug("Received list of THANKS KEEPER achievements, size = {}", achievements.size());
        return achievements;
    }

    private List<String> createThanksKeeperAchievements() {
        logger.debug("Request to keepers repository: get all active keepers");
        List<KeeperDTO> keepers = keeperService.getKeepers();
        logger.debug("Received list of active keepers, size = {} ", keepers.size());
        List<String> result = new ArrayList<>();
        if (keepers.isEmpty()) {
            logger.debug("No any active keepers received from user service");
        } else {
            result = keepers.stream()
                    .map(this::prepareThanksKeeperAchievements)
                    .flatMap(Collection::stream)
                    .map(achievementRepository::addAchievement)
                    .collect(Collectors.toList());
        }
        return result;
    }

    private List<Achievement> prepareThanksKeeperAchievements(KeeperDTO keeper) {
        logger.debug("Preparing THANKS KEEPER achievements for send to repository. {}", keeper);
        return keeper.getDirections().stream()
                .map(direction -> createAchievement(keeper.getUuid(), direction))
                .collect(Collectors.toList());
    }

    private Achievement createAchievement(String uuid, String direction) {
        String description = String.format(THANKS_DESCRIPTION, direction);
        return new Achievement(systemFrom, uuid, KEEPER_THANKS, description, AchievementType.THANKS_KEEPER);
    }

    public List<String> addWelcome(WelcomeRequest request) {
        String userFromId = request.getFrom();
        String userToId = request.getTo();

        logger.debug("Send request to repository: get already created  WELCOME achievement by user '{}'", userToId);
        List<Achievement> welcome = achievementRepository.getWelcomeAchievementByUser(userToId);

        if (!welcome.isEmpty()) {
            logger.warn("User '{}' tried to give 'Welcome' achievement more than one time to {}",
                    userFromId, userToId);
            throw new WelcomeAchievementException(
                    "User tried to give WELCOME achievement more than one time to one person");
        } else {
            Achievement achievement = new Achievement(userFromId, userToId, WELCOME_POINTS, WELCOME_DESCRIPTION,
                    AchievementType.WELCOME);
            logger.debug("Send new WELCOME achievement to repository");
            String id = achievementRepository.addAchievement(achievement);
            logger.info("Added WELCOME achievement from user: '{}', id: [{}]", request.getFrom(), id);
            return Collections.singletonList(id);
        }
    }

    public List<String> addTeam(TeamRequest request) {
        logger.debug("Preparing TEAM achievements for send to repository");
        String userFromId = request.getFrom();
        Set<String> members = request.getMembers();
        List<Achievement> teamAchievements = achievementRepository.getAllTeamAchievementsCurrentWeek(members);
        if (!teamAchievements.isEmpty()) {
            logger.warn("User '{}' tried to give TEAM achievements but some members have such achievements this week",
                    userFromId);
            throw new TeamAchievementException("Cannot add 'Team' achievements. Some team members have such " +
                    " achievements this week.");
        }

        List<String> ids = members.stream()
                .map(userId -> achievementRepository.addAchievement(
                        new Achievement(userFromId, userId, TEAM_POINTS, TEAM_DESCRIPTION, AchievementType.TEAM)))
                .collect(Collectors.toList());

        logger.info("Added TEAM achievements from user '{}', ids: '{}'", userFromId, ids);
        return ids;
    }
}