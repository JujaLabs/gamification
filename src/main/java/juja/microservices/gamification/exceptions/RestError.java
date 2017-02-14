package juja.microservices.gamification.exceptions;

import lombok.Getter;

/**
 * Custom rest-error response
 * Example error response
 *
 * {
 *  "code": 500,
 *  "message": "Test unsupported",
 *  "developerMessage": "For developer message"
 * }
 *
 * @author Danil Kuznetsov
 */
@Getter
public class RestError {
    private final int status;
    private final int code;
    private final String message;
    private final String developerMessage;


    public RestError(int status, int code, String message, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
    }
}
