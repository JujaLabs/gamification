package juja.microservices.gamification.service;


import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AchievementService {

    @Inject
    private AchievementRepository achievementRepository;

    public String addDaily(String description, String userFromId) {
        String userToId = userFromId;
        String currentDate = getFormattedCurrentDate();
        List<Achievement> userFromIdList = achievementRepository.getAllAchievementsByUserToId(userFromId);
        for (Achievement achievement : userFromIdList) {
            if (AchievementType.DAILY.equals(achievement.getType())&&
                    currentDate.equals(achievement.getSendDate()))
            {
                String oldDescription = achievement.getDescription();
                description = oldDescription
                        .concat(System.lineSeparator())
                        .concat(description);
            }
        }
        Achievement newAchievement = new Achievement(userFromId, userToId, 1, description, AchievementType.DAILY);
        return achievementRepository.addAchievement(newAchievement);
    }
    private String getFormattedCurrentDate(){
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentDate);
    }
}
