package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
public class TeamDTO {

    private Set<String> members;

    public TeamDTO(@JsonProperty("members") Set<String> members) {
        this.members = members;
    }
}
