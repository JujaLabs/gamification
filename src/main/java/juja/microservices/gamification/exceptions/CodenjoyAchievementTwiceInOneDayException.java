package juja.microservices.gamification.exceptions;

public class CodenjoyAchievementTwiceInOneDayException extends GamificationException {
    public CodenjoyAchievementTwiceInOneDayException(String message) {
        super(message);
    }
}