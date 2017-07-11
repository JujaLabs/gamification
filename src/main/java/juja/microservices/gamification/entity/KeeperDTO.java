package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Getter
@EqualsAndHashCode
public class KeeperDTO {
    private String uuid;
    private List<String> direction;

    @JsonCreator
    public KeeperDTO(@JsonProperty("uuid") String uuid,
                     @JsonProperty("directions") List<String> direction) {

        this.uuid = uuid;
        this.direction = direction;
    }
}