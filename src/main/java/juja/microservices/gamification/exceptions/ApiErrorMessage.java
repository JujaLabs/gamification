package juja.microservices.gamification.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Message to send client when program throws an exception
 *
 * @author Danil Kuznetsov
 */

@Getter
class ApiErrorMessage {
    /**
     * The status is duplicate http httpStatus internalErrorCode
     */
    private int httpStatus;
    /**
     * The code is internal error code for this exception
     */
    private String internalErrorCode;
    /**
     * The message for user
     */
    private String clientMessage;
    /**
     * The message  for developer
     */
    private String developerMessage;
    /**
     * The message  in exception
     */
    private String exceptionMessage;
    /**
     * List of detail error messages
     */
    private List<String> detailErrors;

     static ApiErrorMessageBuilder builder(ApiErrorStatus apiStatus) {
        return new ApiErrorMessageBuilder(apiStatus);
    }

    private ApiErrorMessage(String code, String clientMessage, String developerMessage) {
        this.httpStatus = 0;
        this.internalErrorCode = code;
        this.clientMessage = clientMessage;
        this.developerMessage = developerMessage;
        this.exceptionMessage = "";
        this.detailErrors = new ArrayList<>();
    }

     static class ApiErrorMessageBuilder {

        private ApiErrorMessage instance;

        private ApiErrorMessageBuilder(ApiErrorStatus apiStatus) {
            instance = new ApiErrorMessage(
                    apiStatus.internalCode(),
                    apiStatus.clientMessage(),
                    apiStatus.developerMessage()
            );
        }

         ApiErrorMessageBuilder httpStatus(int status) {
            instance.httpStatus = status;
            return this;
        }

         ApiErrorMessageBuilder exceptionMessage(String exceptionMessage) {
            instance.exceptionMessage = exceptionMessage;
            return this;
        }

         ApiErrorMessageBuilder detailError(String detailError) {
            instance.detailErrors.add(detailError);
            return this;
        }

         ApiErrorMessageBuilder detailErrors(List<String> detailErrors) {
            instance.detailErrors.addAll(detailErrors);
            return this;
        }

         ApiErrorMessage build() {
            return instance;
        }
    }
}
