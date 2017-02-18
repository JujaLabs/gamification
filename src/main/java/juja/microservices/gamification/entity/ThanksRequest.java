package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class ThanksRequest {

    @Getter
    private String from;
    @Getter
    private String to;
    @Getter
    private String description;

    @JsonCreator
    public ThanksRequest(@JsonProperty("userFromId") String from,@JsonProperty("userToId") String to,
        @JsonProperty("description") String description) {
        this.from = from;
        this.to = to;
        this.description = description;
    }
}
