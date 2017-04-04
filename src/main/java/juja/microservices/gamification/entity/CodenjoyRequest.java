package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
public class CodenjoyRequest {

    @NotEmpty
    private String from;
    @NotEmpty
    private String firstPlace;
    @NotEmpty
    private String secondPlace;
    @NotEmpty
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
