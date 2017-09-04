package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

@Getter
public class TeamRequest {

    @NotEmpty
    private String from;
    @NotEmpty
    private Set<String> members;

    @JsonCreator
    public TeamRequest(@JsonProperty("from") String from, @JsonProperty("members") Set<String> members) {
        this.from = from;
        this.members = members;
    }
}
