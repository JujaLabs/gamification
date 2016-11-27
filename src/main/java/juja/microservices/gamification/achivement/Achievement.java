package juja.microservices.gamification.achivement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class Achievement {

    @Id
    private String id;

    private String userFromId;
    private String userToId;
    private int pointCount;
    private String description;

    @JsonCreator
    public Achievement(@JsonProperty("userFromId") String userFromId, @JsonProperty("userToId") String userToId,
                       @JsonProperty("pointCount") int pointCount, @JsonProperty("description") String description) {
        this.userFromId = userFromId;
        this.userToId = userToId;
        this.pointCount = pointCount;
        this.description = description;
    }

    public String getId() {
        return id;
    }
}
