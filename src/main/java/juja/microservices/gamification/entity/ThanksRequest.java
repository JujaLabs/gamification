package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
public class ThanksRequest {

    @NotEmpty
    private String from;
    @NotEmpty
    private String to;
    @NotEmpty
    private String description;

    @JsonCreator
    public ThanksRequest(@JsonProperty("from") String from, @JsonProperty("to") String to,
        @JsonProperty("description") String description) {
        this.from = from;
        this.to = to;
        this.description = description;
    }
}
