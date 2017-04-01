package juja.microservices.gamification.service;

import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.exceptions.UnsupportedAchievementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestValidator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void checkDaily(DailyRequest request) {
        if (request.getDescription().isEmpty()) {
            String message = "Field 'Description' in Daily request is empty";
            logger.warn(message);
            throw new UnsupportedAchievementException(message);
        }
        logger.info("Daily request successfully checked");
    }

    public void checkThanks(ThanksRequest request) {
        checkNull(request);
        checkFrom(request);
        checkTo(request);
        if (request.getDescription().isEmpty()) {
            String message = "Field 'Description' in Thanks request is empty";
            logger.warn(message);
            throw new UnsupportedAchievementException(message);
        }
        logger.info("Thanks request successfully checked");
    }

    public void checkCodenjoy(CodenjoyRequest request) {
        checkNull(request);
        checkFrom(request);
        checkUsers(request);
        logger.info("Codenjoy request successfully checked");
    }

    public void checkInterview(InterviewRequest request) {
        checkNull(request);
        checkFrom(request);
        if (request.getDescription().isEmpty()) {
            String message = "Field 'Description' in request Interview is empty";
            logger.warn(message);
            throw new UnsupportedAchievementException(message);
        }
        logger.info("Interview request successfully checked");
    }

    private void checkNull(AbstractRequest request) {
        if (request == null) {
            String message = "Received request is null";
            logger.warn(message);
            throw new UnsupportedAchievementException(message);
        }
    }

    private void checkFrom(AbstractRequest request) {
        if (request.getFrom().isEmpty()) {
            logger.warn("Field 'From' in request '{}' is empty", request.getClass().getSimpleName());
            throw new UnsupportedAchievementException("Field 'From' in request is empty");
        }
    }

    private void checkTo(ThanksRequest request) {
        if (request.getTo().isEmpty()) {
            String message = "Field 'To' in Thanks request is empty";
            logger.warn(message);
            throw new UnsupportedAchievementException(message);
        }

        if (request.getFrom().equalsIgnoreCase(request.getTo())) {
            logger.warn("User '{}' trying to put 'Thanks' achievement to yourself", request.getTo());
            throw new UnsupportedAchievementException("You cannot thank yourself");
        }
    }

    private void checkUsers(CodenjoyRequest request) {
        String firstUserToId = request.getFirstPlace();
        String secondUserToId = request.getSecondPlace();
        String thirdUserToId = request.getThirdPlace();

        if ("".equals(firstUserToId) || "".equals(secondUserToId)) {
            logger.warn("Codenjoy request rejected: first or second user is empty");
            throw new UnsupportedAchievementException("First or second place user cannot be empty");
        }
        if (firstUserToId.equalsIgnoreCase(secondUserToId)) {
            logger.warn("Codenjoy request rejected: first and second place users is same");
            throw new UnsupportedAchievementException("First and second users must be different");
        }
        if (firstUserToId.equalsIgnoreCase(thirdUserToId)) {
            logger.warn("Codenjoy request rejected: first and third place users is same");
            throw new UnsupportedAchievementException("First and third users must be different");
        }
        if (secondUserToId.equalsIgnoreCase(thirdUserToId)) {
            logger.warn("Codenjoy request rejected: second and third place users is same");
            throw new UnsupportedAchievementException("Second and third users must be different");
        }
    }
}
