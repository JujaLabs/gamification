package juja.microservices.gamification.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserPointsSum;
import juja.microservices.gamification.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/gamification/user", produces = "application/json")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private UserService userService;

    @GetMapping(value = "/pointSum")
    @ApiOperation(
            value = "Get total points for all users",
            notes = "This method returns total points for all users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns total points for all users"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method")
    })
    public ResponseEntity<?> getAllUsersWithPointSum() {
        logger.debug("Received request /pointSum");
        List<UserPointsSum> users = userService.getAllUsersWithPointSum();
        logger.debug("Response data: users with point sum, quantity = {}", users.size());
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/achieveDetails", consumes = "application/json")
    @ApiOperation(
            value = "Get achievement details for some users",
            notes = "This method returns detailed information for selected users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns full information for selected users"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> getUsersWithAchievementDetails(@RequestBody UserIdsRequest toIds) {
        logger.debug("Received request /achieveDetails");
        List<UserAchievementDetails> result =
                userService.getUserAchievementsDetails(toIds);
        logger.debug("Response data: users with achievement details, quantity = {}", result.size());
        return ResponseEntity.ok(result);
    }
}
