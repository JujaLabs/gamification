package juja.microservices.gamification.achivement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by ВаНо on 08.12.2016.
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
