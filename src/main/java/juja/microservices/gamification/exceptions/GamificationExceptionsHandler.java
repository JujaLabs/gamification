package juja.microservices.gamification.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Danil Kuznetsov
 */

@RestControllerAdvice
public class GamificationExceptionsHandler {
    @ExceptionHandler(GamificationException.class)
    public ResponseEntity<RestErrorMessage> handleBaseGamificationException(GamificationException exception) {

        RestErrorMessage restErrorMessage = new RestErrorMessage(
                HttpStatus.BAD_REQUEST.value(), GamificationErrorStatus.BASE_GAMIFICATION_EXCEPTION.internalCode(),
                GamificationErrorStatus.BASE_GAMIFICATION_EXCEPTION.clientMessage(), exception.getMessage()
        );
        return new ResponseEntity<>(restErrorMessage, HttpStatus.BAD_REQUEST);
    }
}
