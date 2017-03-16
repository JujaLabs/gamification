package juja.microservices.gamification.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
public class Achievement {

    @Id
    private String id;

    private String from;
    private String to;
    @Setter
    private String sendDate;
    private int point;
    @Setter
    private String description;
    private AchievementType type;

    @JsonCreator
    public Achievement(@JsonProperty("from") String from,@JsonProperty("to") String to,
                       @JsonProperty("point") int point, @JsonProperty("description") String description,
                       @JsonProperty("type") AchievementType type) {
        this.from = from;
        this.to = to;
        this.point = point;
        this.description = description;
        this.type = type;
    }

    @Override
    public String toString() {
        String lineSeparator = System.lineSeparator();
        return "Achievement:".concat(lineSeparator)
                .concat("id = ").concat(id).concat(lineSeparator)
                .concat("from = ").concat(from).concat(lineSeparator)
                .concat("to = ").concat(to).concat(lineSeparator)
                .concat("sendDate = ").concat(sendDate).concat(lineSeparator)
                .concat("point = ").concat(Integer.toString(point)).concat(lineSeparator)
                .concat("description = ").concat(description).concat(lineSeparator)
                .concat("type = ").concat(type.toString()).concat(lineSeparator);
    }
}
