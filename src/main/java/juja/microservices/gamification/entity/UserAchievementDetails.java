package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author ВаНо
 */
public class UserAchievementDetails {
    private final String userId;
    private List<Achievement> details;

    @JsonCreator
    public UserAchievementDetails(@JsonProperty("userId") String userId,
                                  @JsonProperty("details") List<Achievement> details) {
        this.userId = userId;
        this.details = details;
    }

    public String getUserId() {
        return userId;
    }

    public List<Achievement> getDetails() {
        return details;
    }
}
