package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import lombok.Getter;

/**
 * @author ВаНо
 */
@Getter
public class UserAchievementDetails {
    private final String userId;
    private List<Achievement> details;

    @JsonCreator
    public UserAchievementDetails(@JsonProperty("userId") String userId,
                                  @JsonProperty("details") List<Achievement> details) {
        this.userId = userId;
        this.details = details;
    }
}
