package juja.microservices.gamification.achievement;

import juja.microservices.gamification.Entity.AchievementDetail;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author ВаНо
 */
public class AchievementDetailTest {
    @Test
    public void idDateExtractorTest(){
        String sendDateGiven = "583d8e106c51051a60928d62";
        AchievementDetail detail =
                new AchievementDetail("userFromId",sendDateGiven,
                        "description",1);
        String sendDateFormatted = detail.getSendDate();
        assertEquals("2016-11-29 16:17:52",sendDateFormatted);
    }
}
