package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class Achievement {

    @Id
    private String id;

    private String userFromId;
    private String userToId;
    private String sendDate;
    private int pointCount;
    private String description;
    private AchievementType type;

    @JsonCreator
    public Achievement(@JsonProperty("userFromId") String userFromId,@JsonProperty("userToId") String userToId,
                       @JsonProperty("pointCount") int pointCount, @JsonProperty("description") String description,
                       @JsonProperty("type") AchievementType type) {
        this.userFromId = userFromId;
        this.userToId = userToId;
        this.pointCount = pointCount;
        this.description = description;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getUserToId() {
        return userToId;
    }

    public int getPointCount() {
        return pointCount;
    }

    public String getUserFromId() {
        return userFromId;
    }

    public String getDescription() {
        return description;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public AchievementType getType() {
        return type;
    }
}
