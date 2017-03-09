package juja.microservices.gamification.controller;

import java.util.List;

import juja.microservices.gamification.entity.CodenjoyRequest;
import juja.microservices.gamification.entity.DailyRequest;
import juja.microservices.gamification.entity.ThanksRequest;
import juja.microservices.gamification.entity.InterviewRequest;
import juja.microservices.gamification.service.AchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * @author Olga Kulykova
 */
@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class AchievementController {

    @Inject
    private AchievementService achievementService;

    @RequestMapping(value = "/achieve/daily", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addDaily(@RequestBody DailyRequest request) {
        String achievementId = achievementService.addDaily(request);
        return ResponseEntity.ok(achievementId);
    }

    @RequestMapping(value = "/achieve/thanks", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addThanks(@RequestBody ThanksRequest request) {
        List<String> ids = achievementService.addThanks(request);
        return ResponseEntity.ok(ids);
    }

    @RequestMapping(value = "/achieve/codenjoy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addCodenjoy(@RequestBody CodenjoyRequest request) {
        List<String> ids = achievementService.addCodenjoy(request);
        return ResponseEntity.ok(ids);
    }

    @RequestMapping(value = "/achieve/interview", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addInterview(@RequestBody InterviewRequest request) {
        String achivementId = achievementService.addInterview(request);
        return ResponseEntity.ok(achivementId);
    }
}
