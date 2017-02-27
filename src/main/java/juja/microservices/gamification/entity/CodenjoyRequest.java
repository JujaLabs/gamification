package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CodenjoyRequest {

    private String from;
    private String firstPlace;
    private String secondPlace;
    private String thirdPlace;

    @JsonCreator
    public CodenjoyRequest(@JsonProperty("from") String from, @JsonProperty("firstPlace") String firstPlace,
            @JsonProperty("secondPlace") String secondPlace, @JsonProperty("thirdPlace") String thirdPlace) {
        this.from = from;
        this.firstPlace = firstPlace;
        this.secondPlace = secondPlace;
        this.thirdPlace = thirdPlace;
    }
}
