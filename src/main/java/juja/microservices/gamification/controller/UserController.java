package juja.microservices.gamification.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserPointsSum;
import juja.microservices.gamification.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

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
        List<UserPointsSum> users = userService.getAllUsersWithPointSum();

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
        List<UserAchievementDetails> result =
                userService.getUserAchievementsDetails(toIds);

        return ResponseEntity.ok(result);
    }
}
