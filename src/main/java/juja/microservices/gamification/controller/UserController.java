package juja.microservices.gamification.controller;


import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserPointsSum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class UserController {

    @Inject
    private AchievementRepository achievementRepository;

    @RequestMapping(value = "/user/achieveSum", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllUsersWithAchievement() {
        List<UserPointsSum> users = achievementRepository.getAllUsersWithAchievement();

        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping (value = "/user/achieveDetails", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUsersWithAchievementDetails(){
        List<UserAchievementDetails> result =
                achievementRepository.getUserAchievementsDetails();
        return ResponseEntity.ok(result);
    }
}
