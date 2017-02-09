package juja.microservices.gamification.service;


import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class AchievementService {

    @Inject
    private AchievementRepository achievementRepository;

    /**
     * In this method userFromId = userToId because users add DAILY achievements to themselves.
     * If the DAILY achievement already exists in the database and user wants to add another DAILY
     * achievement at the same day, the only field description will be updated.
     */
    //TODO в системе может существовать только 1 дейлик за один конкретный день. Иначе -?
    public String addDaily(String description, String userFromId) {
        List<Achievement> userFromIdList = achievementRepository.getAllAchievementsByUserFromIdCurrentDateType(userFromId, AchievementType.DAILY);

        if (userFromIdList.size()==0){
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

}
