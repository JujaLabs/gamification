package juja.microservices.gamification.achivement;

import java.util.List;

/**
 * Created by ВаНо on 08.12.2016.
 */
public class UserAchievementDetails {
    private final String userId;
    private List<AchievementDetail> details;

    public UserAchievementDetails(String userId, List<AchievementDetail> details) {
        this.userId = userId;
        this.details = details;
    }
}
