package juja.microservices.gamification.exceptions;

/**
 * @author Danil Kuznetsov
 */
public class GamificationException extends RuntimeException {
    public GamificationException(String message) {
        super(message);
    }
}
