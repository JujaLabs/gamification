package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThanksRequest {

    private String userFrom;
    private String userTo;
    private String description;

    @JsonCreator
    public ThanksRequest(@JsonProperty("userFromId") String userFrom,@JsonProperty("userToId") String userTo,
        @JsonProperty("description") String description) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.description = description;
    }

    public String getFrom() {
        return this.userFrom;
    }

    public String getTo() {
        return this.userTo;
    }

    public String getDescription() {
        return this.description;
    }
}
