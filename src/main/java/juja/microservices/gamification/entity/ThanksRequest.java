package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ThanksRequest extends AbstractRequest {

    private String to;
    private String description;

    @JsonCreator
    public ThanksRequest(@JsonProperty("from") String from, @JsonProperty("to") String to,
        @JsonProperty("description") String description) {
        this.from = from;
        this.to = to;
        this.description = description;
    }
}
