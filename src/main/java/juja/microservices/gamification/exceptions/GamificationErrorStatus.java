package juja.microservices.gamification.exceptions;

/**
 * @author Danil Kuznetsov
 */
public enum GamificationErrorStatus {
    GAMIFICATION_EXCEPTION(0, "Oops something went wrong :(", "The exception is general in gamification"),

    STANDARD_SPRING_EXCEPTION(1, "Oops something went wrong :(", "The exception is  standard Spring exceptions"),

    SPRING_NOT_VALID_REQUEST_EXCEPTION (
            2,
            "You request is not valid",
            "The exception is Spring exceptions: BindException or MethodArgumentNotValidException"
    ),

    SPRING_REQUEST_PARAMETER_NOT_FOUND_EXCEPTION (
            3,
            "In your request is missing parameters",
            "The exception is Spring exceptions: MissingServletRequestPartException or MissingServletRequestParameterException"
    );

    private String developerMessage;
    private String clientMessage;
    private int internalCode;

    GamificationErrorStatus(int internalCode, String clientMessage, String developerMessage) {
        this.internalCode = internalCode;
        this.clientMessage = clientMessage;
        this.developerMessage = developerMessage;
    }

    public int internalCode() {
        return internalCode;
    }

    public String clientMessage() {
        return clientMessage;
    }

    public String developerMessage() {
        return developerMessage;
    }
}
