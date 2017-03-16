package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@Getter
public class InterviewRequest {
    private String from;
    private String description;

    @JsonCreator
    public InterviewRequest(@JsonProperty("from") String from, @JsonProperty("description") String description) {
        this.from = from;
        this.description = description;
    }
}
