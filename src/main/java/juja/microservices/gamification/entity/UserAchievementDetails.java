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
    private final String user;
    private List<Achievement> details;

    @JsonCreator
    public UserAchievementDetails(@JsonProperty("user") String user,
                                  @JsonProperty("details") List<Achievement> details) {
        this.user = user;
        this.details = details;
    }
}
