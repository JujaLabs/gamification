package juja.microservices.gamification.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ВаНо
 */
public class AchievementDetail {

    private String userFromId;
    private String sendDate;
    private String description;
    private int pointCount;

    @JsonCreator
    public AchievementDetail(@JsonProperty("userFromId") String userFromId, @JsonProperty("_id") String sendDate,
                             @JsonProperty("description") String description, @JsonProperty("pointCount") int pointCount) {
        this.userFromId = userFromId;
        this.sendDate = idDateExtractor(sendDate);
        this.description = description;
        this.pointCount = pointCount;
    }

    private String idDateExtractor(String mongoId){
        String result = mongoId.substring(0,8);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(result,16)*1000);
        result = formatter.format(date);
        return result;
    }

    public String getUserFromId() {
        return userFromId;
    }

    public String getSendDate() {
        return sendDate;
    }

    public String getDescription() {
        return description;
    }

    public int getPointCount() {
        return pointCount;
    }
}
