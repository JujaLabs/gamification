package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Keeper {
    private String uuid;
    private String description;
    private String from;

    @JsonCreator
    public Keeper (@JsonProperty("uuid") String uuid,
                   @JsonProperty("description") String description,
                   @JsonProperty("from") String from) {

        this.uuid = uuid;
        this.description = description;
        this.from = from;
    }
}