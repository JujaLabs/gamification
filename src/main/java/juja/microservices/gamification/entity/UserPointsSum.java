package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserPointsSum {

    private String userToId;
    private int pointCount;

    @JsonCreator
    public UserPointsSum(@JsonProperty("userToId") String userToId,
                         @JsonProperty("pointCount") int pointCount) {
        this.userToId = userToId;
        this.pointCount = pointCount;
    }
}
