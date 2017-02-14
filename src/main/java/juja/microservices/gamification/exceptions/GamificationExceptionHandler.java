package juja.microservices.gamification.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Danil Kuznetsov
 */

@RestControllerAdvice
public class GamificationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GamificationException.class)
    public ResponseEntity<RestError> handle(GamificationException ex) {
        RestError restError = new RestError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(), "For developer message"
        );
        return new ResponseEntity<RestError>(restError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
