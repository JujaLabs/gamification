package juja.microservices.gamification.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danil Kuznetsov
 */

@RestControllerAdvice
public class GamificationExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GamificationException.class)
    public ResponseEntity<RestErrorMessage> handleGamificationException(GamificationException exception) {

        RestErrorMessage restErrorMessage = new RestErrorMessage(
                HttpStatus.BAD_REQUEST.value(), GamificationErrorStatus.GAMIFICATION_EXCEPTION.internalCode(),
                GamificationErrorStatus.GAMIFICATION_EXCEPTION.clientMessage(), exception.getMessage()
        );
        return new ResponseEntity<>(restErrorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Scenarios of a client send an invalid request to API
     * Handler:
     * BindException
     * MethodArgumentNotValidException
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " : " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + " : " + error.getDefaultMessage());
        }

        StringBuilder builder = new StringBuilder();
        builder.append(GamificationErrorStatus.SPRING_NOT_VALID_REQUEST_EXCEPTION.developerMessage());
        builder.append("Exception message:");
        builder.append(ex.getMessage());

        RestErrorMessage errorMessage = new RestErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                GamificationErrorStatus.SPRING_NOT_VALID_REQUEST_EXCEPTION.internalCode(),
                GamificationErrorStatus.SPRING_NOT_VALID_REQUEST_EXCEPTION.clientMessage(),
                builder.toString(),
                errors
        );

        return handleExceptionInternal(ex, errorMessage, headers, HttpStatus.valueOf(errorMessage.getStatus()), request);
    }

    /**
     * Scenarios when part of multipart request not found or when request missing parameter
     * Handler:
     * MissingServletRequestPartException
     * MissingServletRequestParameterException
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {

        String error = ex.getParameterName() + " parameter is missing";

        StringBuilder builder = new StringBuilder();
        builder.append(GamificationErrorStatus.SPRING_REQUEST_PARAMETER_NOT_FOUND_EXCEPTION.developerMessage());
        builder.append("Exception message:");
        builder.append(ex.getMessage());

        RestErrorMessage errorMessage = new RestErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                GamificationErrorStatus.SPRING_REQUEST_PARAMETER_NOT_FOUND_EXCEPTION.internalCode(),
                GamificationErrorStatus.SPRING_REQUEST_PARAMETER_NOT_FOUND_EXCEPTION.clientMessage(),
                builder.toString(),
                error
        );

        return handleExceptionInternal(ex, errorMessage, headers, HttpStatus.valueOf(errorMessage.getStatus()), request);
    }
}
