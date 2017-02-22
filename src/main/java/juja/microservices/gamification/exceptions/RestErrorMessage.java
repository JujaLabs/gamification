package juja.microservices.gamification.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Message to send client when program throws an exception
 *
 * @author Danil Kuznetsov
 */

@Getter
@AllArgsConstructor
class RestErrorMessage {
    /**
     * The status is duplicate http status code
     */
    private int status;
    /**
     * The code is internal error code for this exception
     */
    private int code;
    /**
     * The message for user
     */
    private String message;
    /**
     * The message  for developer
     */
    private String developerMessage;
    /**
     * List of errors messages
     */
    private List<String> errors;

    RestErrorMessage(int status, int code, String message, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
    }
}
