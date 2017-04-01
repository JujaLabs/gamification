package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;


@Getter
public class InterviewRequest {

    @NotEmpty
    private String from;
    @NotEmpty
    private String description;

    @JsonCreator
    public InterviewRequest(@JsonProperty("from") String from, @JsonProperty("description") String description) {
        this.from = from;
        this.description = description;
    }
}
