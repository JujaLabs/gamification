package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
public class Achievement {

    @Id
    private String id;

    private String userFromId;
    private String userToId;
    @Setter
    private String sendDate;
    private int pointCount;
    @Setter
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

    @Override
    public String toString() {
        String lineSeparator = System.lineSeparator();
        return "Achievement:".concat(lineSeparator)
                .concat("id = ").concat(id).concat(lineSeparator)
                .concat("userFromId = ").concat(userFromId).concat(lineSeparator)
                .concat("userToId = ").concat(userToId).concat(lineSeparator)
                .concat("sendDate = ").concat(sendDate).concat(lineSeparator)
                .concat("pointCount = ").concat(Integer.toString(pointCount)).concat(lineSeparator)
                .concat("description = ").concat(description).concat(lineSeparator)
                .concat("type = ").concat(type.toString()).concat(lineSeparator);
    }
}
