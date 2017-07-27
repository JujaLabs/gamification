package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Set;

@Getter
public class Team {

    private Set<String> members;

    @JsonCreator
    public Team(@JsonProperty("members") Set<String> members) {
        this.members = members;
    }
}
