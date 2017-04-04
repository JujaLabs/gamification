package juja.microservices.gamification.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserPointsSum;
import juja.microservices.gamification.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.util.List;

@RestController
@RequestMapping(value = "/user", consumes = "application/json", produces = "application/json")
public class UserController {

    @Inject
    private UserService userService;

    @RequestMapping(value = "/pointSum", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get total points for all users",
            notes = "This method returns total points for all users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns total points for all users"),
    })
    public ResponseEntity<?> getAllUsersWithPointSum() {
        List<UserPointsSum> users = userService.getAllUsersWithPointSum();

        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/achieveDetails", method = RequestMethod.POST)
    @ApiOperation(
            value = "Get achievement details for some users",
            notes = "This method returns detailed information for selected users"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns full information for selected users"),
    })
    public ResponseEntity<?> getUsersWithAchievementDetails(@RequestBody UserIdsRequest toIds) {
        List<UserAchievementDetails> result =
                userService.getUserAchievementsDetails(toIds);

        return ResponseEntity.ok(result);
    }
}
