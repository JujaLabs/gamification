package juja.microservices.gamification.service;

import java.util.ArrayList;
import java.util.Collections;
import javax.inject.Inject;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AchievementService {

    private static final int TWO_THANKS = 2;
    private static final int CODENJOY_FIRST_PLACE = 5;
    private static final int CODENJOY_SECOND_PLACE = 3;
    private static final int CODENJOY_THIRD_PLACE = 1;

    @Inject
    private AchievementRepository achievementRepository;

    /**
     * In this method userFromId = userToId because users add DAILY achievements to themselves.
     * If the DAILY achievement already exists in the database and user wants to add another DAILY
     * achievement at the same day, the only field description will be updated.
     */
    public String addDaily(String userFromId, String description) {
        List<Achievement> userFromIdList = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType(userFromId, AchievementType.DAILY);

        if (userFromIdList.size() == 0) {
            Achievement newAchievement = new Achievement(userFromId, userFromId, 1, description, AchievementType.DAILY);
            return achievementRepository.addAchievement(newAchievement);
        } else {
            Achievement achievement = userFromIdList.get(0);
            String oldDescription = achievement.getDescription();
            description = oldDescription
                .concat(System.lineSeparator())
                .concat(description);
            achievement.setDescription(description);
            return achievementRepository.addAchievement(achievement);
        }
    }

    public List<String> addThanks(String userFromId, String userToId, String description) {
        List<String> result = new ArrayList<>();
        List<Achievement> userFromAndToListToday = achievementRepository
            .getAllAchievementsByUserFromIdCurrentDateType(userFromId, AchievementType.THANKS);

        if (userFromId.equalsIgnoreCase(userToId)) {
            throw new UnsupportedAchievementException("You cannot thank yourself");
        }

        for (Achievement achievement : userFromAndToListToday) {
            if (achievement.getTo().equals(userToId)) {
                throw new UnsupportedAchievementException("You cannot give more than one thanks for day one person");
            }
        }

        if (userFromAndToListToday.isEmpty()) {
            Achievement firstThanks = new Achievement(userFromId, userToId, 1, description, AchievementType.THANKS);
            result.add(achievementRepository.addAchievement(firstThanks));
            return result;
        } else if (userFromAndToListToday.size() >= TWO_THANKS) {
            throw new UnsupportedAchievementException("You cannot give more than two thanks for day");
        } else {
            Achievement secondThanks = new Achievement(userFromId, userToId, 1, description, AchievementType.THANKS);
            result.add(achievementRepository.addAchievement(secondThanks));
            String descriptionTwoThanks = "Issued two thanks";
            Achievement thirdThanks = new Achievement(userFromId, userFromId, 1, descriptionTwoThanks, AchievementType.THANKS);
            result.add(achievementRepository.addAchievement(thirdThanks));
        }
        return result;
    }

    public List<String> addCodenjoy(CodenjoyRequest request) {
        String userFromId = request.getFrom();
        String firstUserToId = request.getFirstPlace();
        String secondUserToId = request.getSecondPlace();
        String thirdUserToId = request.getThirdPlace();
        List<String> result = new ArrayList<>();
        List<Achievement> codenjoyUsersToday = achievementRepository.getAllCodenjoyAchievementsCurrentDate();
        if (!codenjoyUsersToday.isEmpty()) {
            throw new UnsupportedAchievementException("You cannot give codenjoy points twice a day");
        } else if ("".equals(userFromId)) {
            throw new UnsupportedAchievementException("User from cannot be empty");
        } else if (!"".equals(secondUserToId) && "".equals(firstUserToId)) {
            throw new UnsupportedAchievementException("First user cannot be empty");
        } else if (!"".equals(thirdUserToId) &&  "".equals(secondUserToId)) {
            throw new UnsupportedAchievementException("Second user cannot be empty");
        } else if (!"".equals(secondUserToId) && firstUserToId.equalsIgnoreCase(secondUserToId)) {
            throw new UnsupportedAchievementException("First and second users must be different");
        } else if (!"".equals(thirdUserToId) && firstUserToId.equalsIgnoreCase(thirdUserToId)) {
            throw new UnsupportedAchievementException("First and third users must be different");
        } else if (!"".equals(secondUserToId) && !"".equals(thirdUserToId)
                && secondUserToId.equalsIgnoreCase(thirdUserToId)) {
            throw new UnsupportedAchievementException("Second and third users must be different");
        }
        Achievement firstPlace = new Achievement(userFromId, firstUserToId, CODENJOY_FIRST_PLACE,
                "Codenjoy first place", AchievementType.CODENJOY);
        result.add(achievementRepository.addAchievement(firstPlace));
        if (!"".equals(secondUserToId)) {
            Achievement secondPlace = new Achievement(userFromId, secondUserToId, CODENJOY_SECOND_PLACE,
                "Codenjoy second place", AchievementType.CODENJOY);
            result.add(achievementRepository.addAchievement(secondPlace));
        }
        if (!"".equals(thirdUserToId)) {
            Achievement thirdPlace = new Achievement(userFromId, thirdUserToId, CODENJOY_THIRD_PLACE,
                "Codenjoy third place", AchievementType.CODENJOY);
            result.add(achievementRepository.addAchievement(thirdPlace));
        }
        return result;
    }
}
