package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

@Getter
public class AdminAddRequest {

    @Setter
    private AchievementType type;
    private String from;
    private String to;
    private String description;

    private String firstPlace;
    private String secondPlace;
    private String thirdPlace;

    private Set<String> members;

    @JsonCreator
    public AdminAddRequest(
            @JsonProperty("type") AchievementType type,
            @JsonProperty("from") String from,
            @JsonProperty("to") String to,
            @JsonProperty("description") String description,
            @JsonProperty("firstPlace") String firstPlace,
            @JsonProperty("secondPlace") String secondPlace,
            @JsonProperty("thirdPlace") String thirdPlace,
            @JsonProperty("members") Set<String> members) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.description = description;
        this.firstPlace = firstPlace;
        this.secondPlace = secondPlace;
        this.thirdPlace = thirdPlace;
        this.members = members;
    }
}
