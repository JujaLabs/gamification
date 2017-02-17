package juja.microservices.gamification.exceptions;

/**
 * @author Danil Kuznetsov
 */

public class UnsupportedAchievementException extends GamificationException {
    public UnsupportedAchievementException(String message) {
        super(message);
    }
}
