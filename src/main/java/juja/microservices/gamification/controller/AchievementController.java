package juja.microservices.gamification.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.entity.DailyRequest;
import juja.microservices.gamification.entity.InterviewRequest;
import juja.microservices.gamification.entity.ThanksRequest;
import juja.microservices.gamification.service.AchievementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * @author Olga Kulykova
 */
@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class AchievementController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private AchievementService achievementService;

    @RequestMapping(value = "/achieve/daily", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(
            value = "Add points for daily report",
            notes = "This method adds points for daily report"
    )
    //TODO @ApiImplicitParams doesn`t works
    //    @ApiImplicitParams(value - @ApiImplicitParam(name, value, dataType, required))
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns achievement id.")
    })
    public ResponseEntity<?> addDaily(@RequestBody DailyRequest request) {
        String achievementId = achievementService.addDaily(request);
        logger.info("Added daily achievement, id = {}", achievementId);
        return ResponseEntity.ok(achievementId);
    }

    @RequestMapping(value = "/achieve/thanks", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(
            value = "Add points for thanks one user by another",
            notes = "This method adds points for help one user by another"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "First thanks per day returns  achievement id.\n" +
                    "Second thanks per day return array of achievement ids."),
    })
    public ResponseEntity<?> addThanks(@RequestBody ThanksRequest request) {
        List<String> ids = achievementService.addThanks(request);
        logger.info("Added thanks achievement, ids = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @RequestMapping(value = "/achieve/codenjoy", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(
            value = "Add points for codenjoy winners",
            notes = "This method adds points for winners in codenjoy tournament"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns array of achievement ids."),
    })
    public ResponseEntity<?> addCodenjoy(@RequestBody CodenjoyRequest request) {
        List<String> ids = achievementService.addCodenjoy(request);
        logger.info("Added codenjoy achievement, ids = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @RequestMapping(value = "/achieve/interview", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(
            value = "Add points for interview",
            notes = "This method adds points for successful or unsuccessful interview"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns  achievement id."),
    })
    public ResponseEntity<?> addInterview(@RequestBody InterviewRequest request) {
        String achievementId = achievementService.addInterview(request);
        logger.info("Added daily achievement, id = {}", achievementId);
        return ResponseEntity.ok(achievementId);
    }
}
