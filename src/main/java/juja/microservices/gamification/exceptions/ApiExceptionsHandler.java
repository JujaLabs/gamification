package juja.microservices.gamification.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danil Kuznetsov
 */

@RestControllerAdvice
public class ApiExceptionsHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorMessage> handleAll(Exception ex) {
        ApiErrorMessage message =
                ApiErrorMessage.builder(ApiErrorStatus.OTHER_EXCEPTION)
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .exceptionMessage(ex.getMessage())
                        .build();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GamificationException.class)
    public ResponseEntity<ApiErrorMessage> handleGamificationException(GamificationException ex) {
        ApiErrorMessage message =
                ApiErrorMessage.builder(ApiErrorStatus.GAMIFICATION_EXCEPTION)
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .exceptionMessage(ex.getMessage())
                        .build();
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ThanksAchievementMoreThanOneException.class)
    public ResponseEntity<ApiErrorMessage> handleThanksAchievementMoreThanOneException(
            ThanksAchievementMoreThanOneException ex) {
        ApiErrorMessage message =
                ApiErrorMessage.builder(ApiErrorStatus.THANKS_ACHIEVEMENT_MORE_THAN_ONE_THANKS_EXCEPTION)
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .exceptionMessage(ex.getMessage())
                        .build();
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ThanksAchievementTryToThanksYourselfException.class)
    public ResponseEntity<ApiErrorMessage> handleThanksAchievementTryToThanksYourselfException(
            ThanksAchievementTryToThanksYourselfException ex) {
        ApiErrorMessage message =
                ApiErrorMessage.builder(ApiErrorStatus.THANKS_ACHIEVEMENT_TRY_TO_THANKS_YOURSELF_EXCEPTION)
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .exceptionMessage(ex.getMessage())
                        .build();
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ThanksAchievementMoreThanTwoException.class)
    public ResponseEntity<ApiErrorMessage> handleThanksAchievementMoreThanTwoException(
            ThanksAchievementMoreThanTwoException ex) {
        ApiErrorMessage message =
                ApiErrorMessage.builder(ApiErrorStatus.THANKS_ACHIEVEMENT_MORE_THAN_TWO_THANKS_EXCEPTION)
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .exceptionMessage(ex.getMessage())
                        .build();
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        ApiErrorMessage message = convertToApiErrorMessage(ex, status);
        logger.warn(message.getExceptionMessage());
        return super.handleExceptionInternal(ex, message, headers, status, request);
    }


    private ApiErrorMessage convertToApiErrorMessage(Exception ex, HttpStatus status) {

        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return messageHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, status);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return messageHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, status);
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            return messageHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, status);
        } else if (ex instanceof MissingPathVariableException) {
            return messageMissingPathVariable((MissingPathVariableException) ex, status);
        } else if (ex instanceof MissingServletRequestParameterException) {
            return messageMissingServletRequestParameter((MissingServletRequestParameterException) ex, status);
        } else if (ex instanceof ConversionNotSupportedException) {
            return messageConversionNotSupported((ConversionNotSupportedException) ex, status);
        } else if (ex instanceof TypeMismatchException) {
            return messageTypeMismatch((TypeMismatchException) ex, status);
        } else if (ex instanceof MethodArgumentNotValidException) {
            return messageMethodArgumentNotValid((MethodArgumentNotValidException) ex, status);
        } else if (ex instanceof MissingServletRequestPartException) {
            return messageMissingServletRequestPart((MissingServletRequestPartException) ex, status);
        } else if (ex instanceof BindException) {
            return messageBindException((BindException) ex, status);
        } else if (ex instanceof NoHandlerFoundException) {
            return messageNoHandlerFoundException((NoHandlerFoundException) ex, status);
        } else {
            return messageOtherSpringExceptions(ex, status);
        }
    }

    private ApiErrorMessage messageNoHandlerFoundException(NoHandlerFoundException ex, HttpStatus httpStatus) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_NO_HANDLER_FOUND_EXCEPTION)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(error)
                .build();
    }

    private ApiErrorMessage messageMissingPathVariable(MissingPathVariableException ex, HttpStatus httpStatus) {
        String error = ex.getVariableName() + " uri variable is missing";

        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_PATH_VARIABLE_NOT_FOUND_EXCEPTION)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(error)
                .build();
    }

    private ApiErrorMessage messageHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpStatus httpStatus) {
        StringBuilder builder = new StringBuilder();
        builder.append(" Media type is not acceptable. Acceptable media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(builder.toString())
                .build();
    }

    private ApiErrorMessage messageHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpStatus httpStatus) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_HTTP_MEDIA_TYPE_NOT_SUPPORTED)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(builder.toString())
                .build();
    }

    private ApiErrorMessage messageHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpStatus httpStatus) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));

        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_HTTP_REQUEST_METHOD_NOT_SUPPORTED)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(builder.toString())
                .build();
    }

    private ApiErrorMessage messageConversionNotSupported(ConversionNotSupportedException ex, HttpStatus httpStatus) {
        String error = ex.getPropertyName() + " should be of type " + ex.getRequiredType().getName();
        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_TYPE_MISMATCH)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(error)
                .build();
    }

    private ApiErrorMessage messageTypeMismatch(TypeMismatchException ex, HttpStatus httpStatus) {
        String error = ex.getPropertyName() + " should be of type " + ex.getRequiredType().getName();
        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_TYPE_MISMATCH)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(error)
                .build();
    }

    private ApiErrorMessage messageBindException(BindException ex, HttpStatus httpStatus) {
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

    private ApiErrorMessage messageMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpStatus httpStatus) {
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

    private ApiErrorMessage messageMissingServletRequestPart(MissingServletRequestPartException ex, HttpStatus httpStatus) {
        String error = ex.getRequestPartName() + " parameter is missing";

        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_REQUEST_PARAMETER_NOT_FOUND_EXCEPTION)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(error)
                .build();
    }

    private ApiErrorMessage messageMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                  HttpStatus httpStatus) {
        String error = ex.getParameterName() + " parameter is missing";

        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_REQUEST_PARAMETER_NOT_FOUND_EXCEPTION)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .detailError(error)
                .build();
    }

    private ApiErrorMessage messageOtherSpringExceptions(Exception ex, HttpStatus httpStatus) {
        return ApiErrorMessage
                .builder(ApiErrorStatus.SPRING_EXCEPTION)
                .httpStatus(httpStatus.value())
                .exceptionMessage(ex.getMessage())
                .build();
    }

}
