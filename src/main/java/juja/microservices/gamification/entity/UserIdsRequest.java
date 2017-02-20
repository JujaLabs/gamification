package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class UserIdsRequest {
    private List<String> toIds;

    @JsonCreator
    public UserIdsRequest() {
    }

    @JsonCreator
    public UserIdsRequest(@JsonProperty List<String> toIds) {
        this.toIds = toIds;
    }
}
