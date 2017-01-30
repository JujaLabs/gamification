package juja.microservices.gamification;

import juja.microservices.gamification.Entity.UserPointsSum;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserPointsSumTest {

    @Test
    public void shouldReturnUserAchievement() {
        String name = "johnny";
        int pointSum = 10;

        UserPointsSum userPointsSum = new UserPointsSum("johnny", 10);

        assertNotNull(userPointsSum);
        assertEquals(name, userPointsSum.getUserToId());
        assertEquals(pointSum, userPointsSum.getPointCount());
    }
}
