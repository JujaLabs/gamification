package juja.microservices.gamification.controller;

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
import javax.validation.Valid;
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
    public ResponseEntity<?> addDaily(@Valid @RequestBody DailyRequest request) {
        String achievementId = achievementService.addDaily(request);
        logger.info("Added daily achievement, id = {}", achievementId);
        return ResponseEntity.ok(achievementId);
    }

    @RequestMapping(value = "/achieve/thanks", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addThanks(@Valid @RequestBody ThanksRequest request) {
        List<String> ids = achievementService.addThanks(request);
        logger.info("Added thanks achievement, ids = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @RequestMapping(value = "/achieve/codenjoy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addCodenjoy(@Valid @RequestBody CodenjoyRequest request) {
        List<String> ids = achievementService.addCodenjoy(request);
        logger.info("Added codenjoy achievement, ids = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @RequestMapping(value = "/achieve/interview", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addInterview(@Valid @RequestBody InterviewRequest request) {
        String achievementId = achievementService.addInterview(request);
        logger.info("Added daily achievement, id = {}", achievementId);
        return ResponseEntity.ok(achievementId);
    }
}
