package juja.microservices.gamification.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
public class Team {

    private Set<String> members;

    public Team(Set<String> members) {
        this.members = members;
    }
}
