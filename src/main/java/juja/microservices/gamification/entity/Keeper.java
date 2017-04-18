package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Keeper {
    private String uuid;
    private String keeperOf;
    private String from;

    @JsonCreator
    public Keeper (@JsonProperty("uuid")String uuid,
                   @JsonProperty("description")String keeperOf,
                   @JsonProperty("from")String from) {

        this.uuid = uuid;
        this.keeperOf = keeperOf;
        this.from = from;
    }
}
