package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserPointsSum {

    private String to;
    private int point;

    @JsonCreator
    public UserPointsSum(@JsonProperty("to") String to,
                         @JsonProperty("point") int point) {
        this.to = to;
        this.point = point;
    }
}
