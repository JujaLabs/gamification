package juja.microservices.gamification.controller;

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
import java.util.List;

@RestController
@RequestMapping(value = "/user",consumes = "application/json", produces = "application/json")
public class UserController {

    @Inject
    private UserService userService;

    @RequestMapping(value = "/pointSum", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsersWithPointSum() {
        List<UserPointsSum> users = userService.getAllUsersWithPointSum();

        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/achieveDetails", method = RequestMethod.POST)
    public ResponseEntity<?> getUsersWithAchievementDetails(@RequestBody UserIdsRequest toIds) {
        List<UserAchievementDetails> result =
                userService.getUserAchievementsDetails(toIds);

        return ResponseEntity.ok(result);
    }
}
