package juja.microservices.gamification.achivement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ВаНо on 08.12.2016.
 */
public class AchievementDetail {

    private String userFromId;
    private String sendDate;
    private String description;
    private int pointCount;

    @JsonCreator
    public AchievementDetail(@JsonProperty("userFromId") String userFromId, @JsonProperty("sendDate") String sendDate,
                             @JsonProperty("description") String description, @JsonProperty("pointCount") int pointCount) {
        this.userFromId = userFromId;
        this.sendDate = sendDate;
        this.description = description;
        this.pointCount = pointCount;
    }
}
