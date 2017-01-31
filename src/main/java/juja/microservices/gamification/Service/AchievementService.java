package juja.microservices.gamification.Service;

import juja.microservices.gamification.Entity.Achievement;
import juja.microservices.gamification.Entity.AchievementType;
import juja.microservices.gamification.dao.AchievementRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class AchievementService {

    @Inject
    private AchievementRepository achievementRepository;

    public String addDaily(String report, String userFromId) {
        //TODO To check if the daily is given today

        Achievement achievement = new Achievement(userFromId, null, 1, report, AchievementType.DAILY);
        return achievementRepository.addAchievement(achievement);
    }
}
