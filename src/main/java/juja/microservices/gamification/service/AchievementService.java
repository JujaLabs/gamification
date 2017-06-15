package juja.microservices.gamification.service;

import java.util.ArrayList;
import javax.inject.Inject;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private AchievementRepository achievementRepository;
    @Inject
    private KeeperService keeperService;

    /**
     * In this method userFromId = userToId because users add DAILY achievements to themselves.
     * If the DAILY achievement already exists in the database and user wants to add another DAILY
     * achievement at the same day, the only field description will be updated.
     */
    public List<String> addDaily(DailyRequest request) {
        String userFromId = request.getFrom();
        String description = request.getDescription();

        List<Achievement> userFromIdList = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType(
                userFromId, AchievementType.DAILY);
        List<String> result = new ArrayList<>();
        Achievement achievement;
        if (userFromIdList.size() == 0) {
            achievement = new Achievement(userFromId, userFromId, DAILY_POINTS, description, AchievementType.DAILY);
            logger.info("Added new 'Daily' achievement {}, from user '{}'", achievement.getId(), userFromId);
        } else {
            achievement = userFromIdList.get(0);
            String oldDescription = achievement.getDescription();
            description = oldDescription
                    .concat(System.lineSeparator())
                    .concat(description);
            achievement.setDescription(description);

            logger.info("Added description to daily achivement from user '{}'", userFromId);
        }
        result.add(achievementRepository.addAchievement(achievement));
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

        List<Achievement> userFromThanksAchievementToday = achievementRepository
                .getAllAchievementsByUserFromIdCurrentDateType(fromId, AchievementType.THANKS);

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
        result.add(achievementRepository.addAchievement(achievement));

        if (!userFromThanksAchievementToday.isEmpty()) {
            String descriptionTwoThanks = ("You got 'thanks' achievement for thanking to other two users");

            Achievement achievementTwoThanks = new Achievement(fromId, fromId, THANKS_POINTS, descriptionTwoThanks,
                    AchievementType.THANKS);
            result.add(achievementRepository.addAchievement(achievementTwoThanks));
        }

        logger.info("Added 'Thanks' achievements '{}'", result.toString());
        return result;
    }

    public List<String> addCodenjoy(CodenjoyRequest request) {
        checkUsers(request);
        String userFromId = request.getFrom();
        List<Achievement> codenjoyUsersToday = achievementRepository.getAllCodenjoyAchievementsCurrentDate();

        if (!codenjoyUsersToday.isEmpty()) {
            logger.warn("User '{}' tried to give 'Codenjoy' achievement points twice a day", userFromId);
            throw new CodenjoyAchievementTwiceInOneDayException("User tried to give 'Codenjoy' achievement points twice a day");
        }

        return addCodenjoyAchievement(request);
    }

    private void checkUsers(CodenjoyRequest request) {
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
    }

    private List<String> addCodenjoyAchievement(CodenjoyRequest request) {
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
        logger.info("Added fist place 'Codenjoy' achievement from user '{}' to '{}'", userFromId, userFirstPlace);
        result.add(achievementRepository.addAchievement(secondPlace));
        logger.info("Added second place 'Codenjoy' achievement from user '{}' to '{}'", userFromId, userSecondPlace);
        result.add(achievementRepository.addAchievement(thirdPlace));
        logger.info("Added third place 'Codenjoy' achievement from user '{}' to '{}'", userFromId, userThirdPlace);

        return result;
    }

    public List<String> addInterview(InterviewRequest request) {
        String userFromId = request.getFrom();
        String description = request.getDescription();
        Achievement newAchievement = new Achievement(userFromId, userFromId, INTERVIEW_POINTS, description,
                AchievementType.INTERVIEW);
        logger.info("Added 'Interview' achievement from user '{}'", userFromId);
        List<String> result = new ArrayList<>();
        result.add(achievementRepository.addAchievement(newAchievement));
        return result;
    }

    public List<String> addThanksKeeper() {
        List<Achievement> achievements = achievementRepository.getAllThanksKeepersAchievementsCurrentWeek();
        return achievements.isEmpty() ? createThanksKeeperAchievements() : getIdsCreatedAchievement(achievements);
    }

    private List<String> createThanksKeeperAchievements() {
        List<String> result = new ArrayList<>();
        List<Keeper> keepers = keeperService.getKeepers();
        if (keepers.isEmpty()) {
            logger.info("No any active keepers received from user service");
        } else {
            keepers.forEach(keeper -> {
                result.add(achievementRepository.addAchievement(getAchievement(keeper)));
            });
            logger.info("Added 'Thanks Keeper' achievements {}", result.toString());
        }
        return result;
    }

    private List<String> getIdsCreatedAchievement (List<Achievement> achievements) {
        List<String> result = getIds(achievements);
        logger.info("Returned already created 'Thanks Keeper' achievements in current week {}", result.toString());

        return result;
    }

    private Achievement getAchievement(Keeper keeper) {
        return new Achievement(
                keeper.getFrom(),
                keeper.getUuid(),
                KEEPER_THANKS,
                keeper.getDescription(),
                AchievementType.THANKS_KEEPER);
    }

    private List<String> getIds(List<Achievement> achievements) {
        List<String> result = new ArrayList<>();
        achievements.forEach(achievement -> {
            result.add(achievement.getId());
        });
        return result;
    }

    public List<String> addWelcome(WelcomeRequest request) {
        String userFromId = request.getFrom();
        String userToId = request.getTo();

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
            result.add(achievementRepository.addAchievement(newAchievement));
            return result;
        }
    }
}