package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Getter
@EqualsAndHashCode
public class KeeperDTO {
    @NotEmpty
    private String uuid;
    @NotEmpty
    private List<String> directions;

    @JsonCreator
    public KeeperDTO(@JsonProperty("uuid") String uuid,
                     @JsonProperty("directions") List<String> directions) {

        this.uuid = uuid;
        this.directions = directions;
    }
}