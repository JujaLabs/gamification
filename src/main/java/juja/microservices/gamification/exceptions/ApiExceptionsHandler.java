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
public class ApiExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GamificationException.class)
    public ResponseEntity<ApiErrorMessage> handleGamificationException(GamificationException ex) {

        ApiErrorMessage.ApiErrorMessageBuilder errorBuilder = ApiErrorMessage
                .builder(ApiErrorStatus.GAMIFICATION_EXCEPTION);

        errorBuilder.httpStatus(HttpStatus.BAD_REQUEST.value())
                .exceptionMessage(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorBuilder.build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        ApiErrorMessage message = convertToApiErrorMessage(ex, status);
        return super.handleExceptionInternal(ex, message, headers, status, request);
    }


    private ApiErrorMessage convertToApiErrorMessage(Exception ex, HttpStatus status) {
        if (ex instanceof MethodArgumentNotValidException) {
            return messageArgumentNotValid((MethodArgumentNotValidException) ex, status);
        } else if (ex instanceof MissingServletRequestParameterException) {
            return messageMissingServletRequestParameter((MissingServletRequestParameterException) ex, status);
        }

        return null;
    }


    /**
     * Scenarios of a client send an invalid request to API
     * Handler:
     * MethodArgumentNotValidException
     */
    private ApiErrorMessage messageArgumentNotValid(MethodArgumentNotValidException ex, HttpStatus httpStatus) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " : " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + " : " + error.getDefaultMessage());
        }
        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_NOT_VALID_REQUEST_EXCEPTION)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailErrors(errors)
                .build();
    }

    /**
     * Scenarios when part of multipart request not found or when request missing parameter
     * Handler:
     * MissingServletRequestParameterException
     */
    private ApiErrorMessage messageMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                  HttpStatus httpStatus) {

        String error = ex.getParameterName() + " parameter is missing";

        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_NOT_VALID_REQUEST_EXCEPTION)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(error)
                .build();
    }


}
