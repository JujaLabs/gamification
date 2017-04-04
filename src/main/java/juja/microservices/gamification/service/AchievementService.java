package juja.microservices.gamification.service;

import java.util.ArrayList;
import javax.inject.Inject;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementService {

    private static final int TWO_THANKS = 2;
    private static final int INTERVIEW_POINTS = 10;
    private static final int CODENJOY_FIRST_PLACE = 5;
    private static final int CODENJOY_SECOND_PLACE = 3;
    private static final int CODENJOY_THIRD_PLACE = 1;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private AchievementRepository achievementRepository;

    /**
     * In this method userFromId = userToId because users add DAILY achievements to themselves.
     * If the DAILY achievement already exists in the database and user wants to add another DAILY
     * achievement at the same day, the only field description will be updated.
     */
    public String addDaily(DailyRequest request) {
        logger.debug("Entered to 'addDaily' method");

        String userFromId = request.getFrom();
        String description = request.getDescription();
        logger.debug("Received data userFromId: '{}', description: '{}'", userFromId, description);

        List<Achievement> userFromIdList = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType(userFromId, AchievementType.DAILY);

        if (userFromIdList.size() == 0) {
            logger.debug("No one 'Daily' achievement for user '{}' at current date", userFromId);
            Achievement newAchievement = new Achievement(userFromId, userFromId, 1, description, AchievementType.DAILY);
            logger.info("Added new 'Daily' achievement {}, from user '{}'", newAchievement.getId(), userFromId);
            return achievementRepository.addAchievement(newAchievement);
        } else {
            logger.debug("There is already one 'Daily' achievement for user '{}' at current date", userFromId);
            Achievement achievement = userFromIdList.get(0);
            logger.debug("Received achivement '{}'", achievement.getId());
            String oldDescription = achievement.getDescription();
            logger.debug("Recieved old description '{}'", oldDescription);
            description = oldDescription
                    .concat(System.lineSeparator())
                    .concat(description);
            logger.debug("Added new description to old description");
            achievement.setDescription(description);
            logger.debug("Description is set to achievement {}", achievement.getId());

            logger.info("Added description to daily achivement from user '{}'", userFromId);
            return achievementRepository.addAchievement(achievement);
        }
    }

    public List<String> addThanks(ThanksRequest request) {
        String userFromId = request.getFrom();
        String userToId = request.getTo();
        String description = request.getDescription();

        if (userFromId.equalsIgnoreCase(userToId)) {
            logger.warn("User '{}' trying to put 'Thanks' achievement to yourself", request.getTo());
            throw new UnsupportedAchievementException("You cannot thank yourself");
        }

        List<Achievement> userFromAndToListToday = achievementRepository
                .getAllAchievementsByUserFromIdCurrentDateType(userFromId, AchievementType.THANKS);

        for (Achievement achievement : userFromAndToListToday) {
            checkTryToThanksTwice(userFromId, userToId, achievement);
        }

        if (userFromAndToListToday.size() >= TWO_THANKS) {
            logger.warn("User '{}' tried to give 'Thanks' achievement more than two times per day", userFromId);
            throw new UnsupportedAchievementException("You cannot give more than two thanks for day");
        } else if (userFromAndToListToday.isEmpty()) {
            return addFirstThanks(userFromId, userToId, description);
        } else {
            return addSecondThanks(userFromId, userToId, description);
        }
    }

    private void checkTryToThanksTwice(String userFromId, String userToId, Achievement achievement) {
        if (achievement.getTo().equals(userToId)) {
            logger.warn("User '{}' tried to give 'Thanks' achievement more than one times to person '{}'", userFromId, userToId);
            throw new UnsupportedAchievementException("You cannot give more than one thanks for day one person");
        }
    }

    private List<String> addFirstThanks(String userFromId, String userToId, String description) {
        List<String> result = new ArrayList<>();
        Achievement firstThanks = new Achievement(userFromId, userToId, 1, description, AchievementType.THANKS);

        result.add(achievementRepository.addAchievement(firstThanks));
        logger.info("Added first 'Thanks' achievement from user '{}' to user '{}'", userFromId, userToId);

        return result;
    }

    private List<String> addSecondThanks(String userFromId, String userToId, String description) {
        List<String> result = new ArrayList<>();
        Achievement secondThanks = new Achievement(userFromId, userToId, 1, description, AchievementType.THANKS);
        result.add(achievementRepository.addAchievement(secondThanks));
        logger.info("Added second 'Thanks' achievement from user '{}' to user '{}'", userFromId, userToId);

        String descriptionTwoThanks = "Issued two thanks";
        Achievement thirdThanks = new Achievement(userFromId, userFromId, 1, descriptionTwoThanks, AchievementType.THANKS);
        result.add(achievementRepository.addAchievement(thirdThanks));
        logger.info("Added 'Thanks' achievement to user '{}' for the distributed two thanks to other users", userFromId);

        return result;
    }

    public List<String> addCodenjoy(CodenjoyRequest request) {
        checkUsers(request);
        String userFromId = request.getFrom();
        List<Achievement> codenjoyUsersToday = achievementRepository.getAllCodenjoyAchievementsCurrentDate();

        if (!codenjoyUsersToday.isEmpty()) {
            logger.warn("User '{}' tried to give 'Codenjoy' achievement points twice a day", userFromId);
            throw new UnsupportedAchievementException("You cannot give codenjoy points twice a day");
        }

        return addCodenjoyAchievement(request);
    }

    private void checkUsers(CodenjoyRequest request) {
        String firstUserId = request.getFirstPlace();
        String secondUserId = request.getSecondPlace();
        String thirdUserId = request.getThirdPlace();

        if (firstUserId.equalsIgnoreCase(secondUserId)) {
            logger.warn("Codenjoy request rejected: first and second place users is same");
            throw new UnsupportedAchievementException("First and second users must be different");
        }
        if (firstUserId.equalsIgnoreCase(thirdUserId)) {
            logger.warn("Codenjoy request rejected: first and third place users is same");
            throw new UnsupportedAchievementException("First and third users must be different");
        }
        if (secondUserId.equalsIgnoreCase(thirdUserId)) {
            logger.warn("Codenjoy request rejected: second and third place users is same");
            throw new UnsupportedAchievementException("Second and third users must be different");
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

    public String addInterview(InterviewRequest request) {
        String userFromId = request.getFrom();
        String description = request.getDescription();
        Achievement newAchievement = new Achievement(userFromId, userFromId, INTERVIEW_POINTS, description, AchievementType.INTERVIEW);
        logger.info("Added 'Interview' achievement from user '{}'", userFromId);

        return achievementRepository.addAchievement(newAchievement);
    }
}