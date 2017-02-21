package juja.microservices.gamification.service;

import java.util.ArrayList;
import java.util.Collections;
import javax.inject.Inject;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AchievementService {

    private static final int TWO_THANKS = 2;

    @Inject
    private AchievementRepository achievementRepository;

    /**
     * In this method userFromId = userToId because users add DAILY achievements to themselves.
     * If the DAILY achievement already exists in the database and user wants to add another DAILY
     * achievement at the same day, the only field description will be updated.
     */
    public String addDaily(String description, String userFromId) {
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
            if (achievement.getUserToId().equals(userToId)) {
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
}
