package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
public class DailyRequest {

    @NotEmpty (message = "Field 'From' must not be empty")
    private String from;
    @NotEmpty (message = "Field 'Description' must not be empty")
    private String description;

    @JsonCreator
    public DailyRequest(@JsonProperty("from") String from, @JsonProperty("description") String description) {
        this.from = from;
        this.description = description;
    }
}
