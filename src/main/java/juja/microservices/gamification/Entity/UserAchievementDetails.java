package juja.microservices.gamification.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import juja.microservices.gamification.Entity.AchievementDetail;

import java.util.List;

/**
 * @author ВаНо
 */
public class UserAchievementDetails {
    private final String userId;
    private List<AchievementDetail> details;

    @JsonCreator
    public UserAchievementDetails(@JsonProperty("userId") String userId,
                                  @JsonProperty("details") List<AchievementDetail> details) {
        this.userId = userId;
        this.details = details;
    }

    public String getUserId() {
        return userId;
    }

    public List<AchievementDetail> getDetails() {
        return details;
    }
}
