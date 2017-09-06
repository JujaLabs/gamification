package juja.microservices.gamification.entity;

import juja.microservices.WithoutScheduling;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserPointsSumTest implements WithoutScheduling {

    @Test
    public void shouldReturnUserAchievement() {
        String name = "johnny";
        int pointSum = 10;

        UserPointsSum userPointsSum = new UserPointsSum("johnny", 10);

        assertNotNull(userPointsSum);
        assertEquals(name, userPointsSum.getTo());
        assertEquals(pointSum, userPointsSum.getPoint());
    }
}
