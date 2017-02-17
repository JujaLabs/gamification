package juja.microservices.gamification.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Message to send client when program throws an exception
 *
 * @author Danil Kuznetsov
 */

@Getter
@AllArgsConstructor
public class RestErrorMessage {
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
}
