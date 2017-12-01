package juja.microservices.gamification.service.impl;

import javafx.util.Pair;
import juja.microservices.gamification.dao.AchievementRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        log.debug("Send request to repository: get already created DAILY achievement by user '{}' for current date",
                userFromId);
        List<Achievement> userFromIdList = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType(
                userFromId,
                AchievementType.DAILY);
        Achievement achievement;
        if (userFromIdList.size() == 0) {
            log.debug("Received empty data from repository. Creating new DAILY achievement.");
            achievement = new Achievement(userFromId, userFromId, DAILY_POINTS, description, AchievementType.DAILY);
        } else {
            log.debug("Received existing achievement. Id: [{}]", userFromIdList.get(0).getId());
            achievement = userFromIdList.get(0);
            String oldDescription = achievement.getDescription();
            description = oldDescription
                    .concat(System.lineSeparator())
                    .concat(description);
            achievement.setDescription(description);
        }
        log.debug("Send DAILY achievement to repository");
        String id = achievementRepository.addAchievement(achievement);
        log.info("Added DAILY achievement from user: '{}', id: [{}]", request.getFrom(), id);

        return Collections.singletonList(id);
    }

    public List<String> addThanks(ThanksRequest request) {
        List<String> result = new ArrayList<>();

        String fromId = request.getFrom();
        String toId = request.getTo();
        String description = request.getDescription();

        if (fromId.equalsIgnoreCase(toId)) {
            log.warn("User '{}' tried to put THANKS achievement to yourself", request.getTo());
            throw new ThanksAchievementTryToThanksYourselfException("User tried to put THANKS achievement to yourself");
        }
        log.debug("Send request to repository: get already created THANKS achievements by user '{}' for current date",
                fromId);
        List<Achievement> userFromThanksAchievementToday = achievementRepository
                .getAllAchievementsByUserFromIdCurrentDateType(fromId, AchievementType.THANKS);
        log.debug("Received list of THANKS achievements, size = {}", userFromThanksAchievementToday.size());
        if (userFromThanksAchievementToday.size() >= TWO_THANKS) {
            log.warn("User '{}' tried to give THANKS achievement more than two times per day", fromId);
            throw new ThanksAchievementMoreThanTwoException(
                    "User tried to give THANKS achievement more than two times per day");
        }

        for (Achievement achievement : userFromThanksAchievementToday) {
            if (achievement.getTo().equals(toId)) {
                log.warn("User '{}' tried to give THANKS achievement more than one times to person '{}'",
                        fromId,
                        toId);
                throw new ThanksAchievementMoreThanOneException(
                        "User tried to give THANKS achievement more than one times to person");
            }
        }

        Achievement achievement = new Achievement(fromId, toId, THANKS_POINTS, description, AchievementType.THANKS);
        log.debug("Send THANKS achievement to repository");
        result.add(achievementRepository.addAchievement(achievement));

        if (!userFromThanksAchievementToday.isEmpty()) {
            String descriptionTwoThanks = ("You got THANKS achievement for thanking to other two users");
            Achievement achievementTwoThanks = new Achievement(fromId, fromId, THANKS_POINTS, descriptionTwoThanks,
                    AchievementType.THANKS);
            log.debug("Send additional THANKS achievement to repository");
            result.add(achievementRepository.addAchievement(achievementTwoThanks));
        }
        log.info("Added THANKS achievements from user: '{}', ids: {}", fromId, result);

        return result;
    }

    public List<String> addCodenjoy(CodenjoyRequest request) {
        checkUsers(request);
        String userFromId = request.getFrom();
        log.debug("Send request to repository: get already created CODENJOY achievements for current date");
        List<Achievement> codenjoyUsersToday = achievementRepository.getAllCodenjoyAchievementsCurrentDate();
        log.debug("Received list of CODENJOY achievements, size = {}", codenjoyUsersToday.size());
        if (!codenjoyUsersToday.isEmpty()) {
            log.warn("User '{}' tried to give CODENJOY achievement points twice a day", userFromId);
            throw new CodenjoyAchievementTwiceInOneDayException("User tried to give CODENJOY achievement points twice a day");
        }

        List<String> result = addCodenjoyAchievement(request);
        log.info("Added CODENJOY achievements, ids: {}", result);

        return result;
    }

    private void checkUsers(CodenjoyRequest request) {
        log.debug("Verification users in codenjoy request");
        String firstUserId = request.getFirstPlace();
        String secondUserId = request.getSecondPlace();
        String thirdUserId = request.getThirdPlace();

        if (firstUserId.equalsIgnoreCase(secondUserId)) {
            log.warn("CODENJOY request rejected: first and second place users is same");
            throw new CodenjoyAchievementException("First and second users is same");
        }
        if (firstUserId.equalsIgnoreCase(thirdUserId)) {
            log.warn("CODENJOY request rejected: first and third place users is same");
            throw new CodenjoyAchievementException("First and third users is same");
        }
        if (secondUserId.equalsIgnoreCase(thirdUserId)) {
            log.warn("CODENJOY request rejected: second and third place users is same");
            throw new CodenjoyAchievementException("Second and third users is same");
        }
        log.debug("Verification was successful");
    }

    private List<String> addCodenjoyAchievement(CodenjoyRequest request) {
        List<String> result = new ArrayList<>();

        log.debug("Preparing achievement for send to repository");
        String userFromId = request.getFrom();
        result.add(achievementRepository.addAchievement(new Achievement(
                userFromId,
                request.getFirstPlace(),
                CODENJOY_FIRST_PLACE,
                "Codenjoy first place",
                AchievementType.CODENJOY)));
        result.add(achievementRepository.addAchievement(new Achievement(
                userFromId,
                request.getSecondPlace(),
                CODENJOY_SECOND_PLACE,
                "Codenjoy second place",
                AchievementType.CODENJOY)));
        result.add(achievementRepository.addAchievement(new Achievement(
                userFromId,
                request.getThirdPlace(),
                CODENJOY_THIRD_PLACE,
                "Codenjoy third place",
                AchievementType.CODENJOY)));

        return result;
    }

    public List<String> addInterview(InterviewRequest request) {
        String userFromId = request.getFrom();
        log.debug("Send INTERVIEW achievement to repository");
        String id = achievementRepository.addAchievement(new Achievement(
                userFromId,
                userFromId,
                INTERVIEW_POINTS,
                request.getDescription(),
                AchievementType.INTERVIEW));
        log.info("Added INTERVIEW achievement from user: '{}', id: [{}]", request.getFrom(), id);

        return Collections.singletonList(id);
    }


    @Scheduled(cron = "${keeper.thanks.cron.expression}")
    private void addThanksKeeperScheduled() {
        List<Achievement> achievements = getThanksKeeperAchievements();
        if (achievements.isEmpty()) {
            List<String> achievementIds = createThanksKeeperAchievements();
            log.info("Added THANKS KEEPER achievement from scheduled task. Ids: {}", achievementIds);
        }
    }

    public List<String> addThanksKeeper() {
        List<String> achievementIds;

        List<Achievement> achievements = getThanksKeeperAchievements();
        if (achievements.isEmpty()) {
            achievementIds = createThanksKeeperAchievements();
            log.info("Added new THANKS KEEPER achievement. Ids: {}", achievementIds);
        } else {
            achievementIds = achievements
                    .stream()
                    .map(Achievement::getId)
                    .collect(Collectors.toList());
            log.info("Returned already created THANKS KEEPER achievements in current week {}", achievementIds);
        }

        return achievementIds;
    }

    private List<Achievement> getThanksKeeperAchievements() {
        log.debug("Send request to repository: get already created THANKS KEEPER achievements for current week");
        List<Achievement> achievements = achievementRepository.getAllThanksKeepersAchievementsCurrentWeek();
        log.debug("Received list of THANKS KEEPER achievements, size = {}", achievements.size());

        return achievements;
    }

    private List<String> createThanksKeeperAchievements() {
        List<String> result = new ArrayList<>();

        log.debug("Request to keepers repository: get all active keepers");
        List<KeeperDTO> keepers = keeperService.getKeepers();
        log.debug("Received list of active keepers, size = {} ", keepers.size());

        if (keepers.isEmpty()) {
            log.debug("No any active keepers received from user service");
        } else {
            log.debug("Sending 'Thanks Keeper' achievements to repository");
            result = keepers
                    .stream()
                    .collect(Collectors.toMap(KeeperDTO::getUuid, KeeperDTO::getDirections))
                    .entrySet()
                    .stream()
                    .flatMap(map -> map
                            .getValue()
                            .stream()
                            .map(value -> new Pair<>(map.getKey(), value)))
                    .map(pair -> new Achievement(
                            systemFrom,
                            pair.getKey(),
                            KEEPER_THANKS,
                            String.format(THANKS_DESCRIPTION, pair.getValue()),
                            AchievementType.THANKS_KEEPER))
                    .map(achievementRepository::addAchievement)
                    .collect(Collectors.toList());
        }

        return result;
    }

    public List<String> addWelcome(WelcomeRequest request) {
        String userFromId = request.getFrom();
        String userToId = request.getTo();

        log.debug("Send request to repository: get already created  WELCOME achievement by user '{}'", userToId);
        List<Achievement> welcome = achievementRepository.getWelcomeAchievementByUser(userToId);

        if (!welcome.isEmpty()) {
            log.warn("User '{}' tried to give 'Welcome' achievement more than one time to {}",
                    userFromId,
                    userToId);
            throw new WelcomeAchievementException(
                    "User tried to give WELCOME achievement more than one time to one person");
        } else {
            Achievement achievement = new Achievement(
                    userFromId,
                    userToId,
                    WELCOME_POINTS,
                    WELCOME_DESCRIPTION,
                    AchievementType.WELCOME);
            log.debug("Send new WELCOME achievement to repository");
            String id = achievementRepository.addAchievement(achievement);
            log.info("Added WELCOME achievement from user: '{}', id: [{}]", request.getFrom(), id);

            return Collections.singletonList(id);
        }
    }

    public List<String> addTeam(TeamRequest request) {
        log.debug("Preparing TEAM achievements for send to repository");
        String userFromId = request.getFrom();
        Set<String> members = request.getMembers();
        List<Achievement> teamAchievements = achievementRepository.getAllTeamAchievementsCurrentWeek(members);
        if (!teamAchievements.isEmpty()) {
            log.warn("User '{}' tried to give TEAM achievements but some members have such achievements this week",
                    userFromId);
            throw new TeamAchievementException("Cannot add 'Team' achievements. Some team members have such " +
                    " achievements this week.");
        }

        List<String> ids = members
                .stream()
                .map(userId -> achievementRepository.addAchievement(new Achievement(
                        userFromId,
                        userId,
                        TEAM_POINTS,
                        TEAM_DESCRIPTION,
                        AchievementType.TEAM)))
                .collect(Collectors.toList());
        log.info("Added TEAM achievements from user '{}', ids: '{}'", userFromId, ids);

        return ids;
    }
}