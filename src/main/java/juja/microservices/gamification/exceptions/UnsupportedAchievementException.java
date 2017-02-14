package juja.microservices.gamification.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Danil Kuznetsov
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "UnsupportedAchievementException")
public class UnsupportedAchievementException extends RuntimeException {
    public UnsupportedAchievementException(String message) {
        super(message);
    }
}
