package juja.microservices.gamification.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.entity.DailyRequest;
import juja.microservices.gamification.entity.InterviewRequest;
import juja.microservices.gamification.entity.TeamRequest;
import juja.microservices.gamification.entity.ThanksRequest;
import juja.microservices.gamification.entity.WelcomeRequest;
import juja.microservices.gamification.service.AchievementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * @author Olga Kulykova
 * @author Vadim Dyachenko
 */
@RestController
@RequestMapping(value = "/v1/gamification/achievements", consumes = "application/json", produces = "application/json")
public class AchievementController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private AchievementService achievementService;

    @PostMapping(value = "/daily")
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
        logger.info("Received request on /achievements/daily from user: '{}', description: '{}'", request.getFrom(), request.getDescription());
        List<String> ids = achievementService.addDaily(request);
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/thanks")
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
        logger.info("Received request on /achievements/thanks from user: '{}', description: '{}'", request.getFrom(), request.getDescription());
        List<String> ids = achievementService.addThanks(request);
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/codenjoy")
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
        logger.info("Received request on /achievements/codenjoy from user: '{}', places: 1st-'{}', 2nd-'{}', 3rd-'{}'",
                request.getFrom(), request.getFirstPlace(), request.getSecondPlace(), request.getThirdPlace());
        List<String> ids = achievementService.addCodenjoy(request);
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/interview")
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
        logger.info("Received request on /achievements/interview from user: '{}', description: '{}'", request.getFrom(), request.getDescription());
        List<String> ids = achievementService.addInterview(request);
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/keepers/thanks")
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
        logger.info("Received request on /achievements/keepers/thanks");
        List<String> ids = achievementService.addThanksKeeper();
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/welcome")
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
        logger.info("Received request on /achievements/welcome from user: '{}'", request.getFrom());
        List<String> ids = achievementService.addWelcome(request);
        return ResponseEntity.ok(ids);
    }

    @PostMapping(value = "/team")
    @ApiOperation(
            value = "Add team points to active team members",
            notes = "This method adds points to active team"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Return array of achievement ids"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> addTeam(@Valid @RequestBody TeamRequest request) {
        logger.info("Received request on /achievements/team from user: '{}'", request.getFrom());
        List<String> ids = achievementService.addTeam(request);
        return ResponseEntity.ok(ids);
    }
}