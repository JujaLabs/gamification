package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Team {

    private Set<String> members;
}
