package juja.microservices.gamification.controller;

import io.swagger.annotations.*;
import juja.microservices.gamification.entity.*;
import juja.microservices.gamification.service.AchievementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.HttpURLConnection;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Olga Kulykova
 */
@RestController
public class AchievementController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private AchievementService achievementService;

    @PostMapping(value = "/v1/achieve/daily", consumes = "application/json", produces = "application/json")
    @ApiOperation(
            value = "Add points for daily report",
            notes = "This method adds points for daily report"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns array with one achievement id"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> addDaily(@Valid @RequestBody DailyRequest request) {
        logger.debug("Received request on /achive/daily : {}", request);
        List<String> ids = achievementService.addDaily(request);
        logger.info("Added 'Daily' achievement, id = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/v1/achieve/thanks", consumes = "application/json", produces = "application/json")
    @ApiOperation(
            value = "Add points for thanks one user by another",
            notes = "This method adds points for help one user by another"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "First thanks per day returns array with " +
                    "one achievement id.\n" +
                    "Second thanks per day return array of two achievement ids"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> addThanks(@Valid @RequestBody ThanksRequest request) {
        logger.debug("Received request on /achieve/thanks : {}", request);
        List<String> ids = achievementService.addThanks(request);
        logger.info("Added 'Thanks' achievement, ids = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/v1/achieve/codenjoy", consumes = "application/json", produces = "application/json")
    @ApiOperation(
            value = "Add points for codenjoy winners",
            notes = "This method adds points for winners in codenjoy tournament"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns array of achievement ids"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> addCodenjoy(@Valid @RequestBody CodenjoyRequest request) {
        logger.debug("Received request on /achieve/codenjoy : {}", request);
        List<String> ids = achievementService.addCodenjoy(request);
        logger.info("Added 'Codenjoy' achievement, ids = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/v1/achieve/interview", consumes = "application/json", produces = "application/json")
    @ApiOperation(
            value = "Add points for interview",
            notes = "This method adds points for successful or unsuccessful interview"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns array with one achievement id"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> addInterview(@Valid @RequestBody InterviewRequest request) {
        logger.debug("Received request on /achieve/interview : {}", request);
        List<String> ids = achievementService.addInterview(request);
        logger.info("Added 'Interview' achievement, id = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/v1/achieve/keepers/thanks", produces = "application/json")
    @ApiOperation(
            value = "Add points for thanks keeper",
            notes = "This method adds points for successful or unsuccessful thanks keeper"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns array with achievement ids"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> addThanksKeeper() {
        List<String> ids = achievementService.addThanksKeeper();
        logger.info("Added 'Thanks keeper' achievement, id = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/v1/achieve/welcome", consumes = "application/json", produces = "application/json")
    @ApiOperation(
            value = "Add welcome points to new user",
            notes = "This method adds points to new user"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return array of one id"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> addWelcome(@Valid @RequestBody WelcomeRequest request) {
        logger.debug("Received request on /achieve/welcome : {}", request);
        List<String> ids = achievementService.addWelcome(request);
        logger.info("Added welcome achievement, id = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

}