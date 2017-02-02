package juja.microservices.gamification.service;


import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class AchievementService {

    @Inject
    private AchievementRepository achievementRepository;

    public String addDaily(String report, String userFromId) {
        //TODO To check if the daily is given today
        String userToId = userFromId;

        Achievement achievement = new Achievement(userFromId, userToId, 1, report, AchievementType.DAILY);
        return achievementRepository.addAchievement(achievement);
    }
}
