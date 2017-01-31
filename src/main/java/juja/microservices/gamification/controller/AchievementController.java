package juja.microservices.gamification.controller;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.service.AchievementService;
import net.minidev.json.JSONObject;
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
    @Inject
    private AchievementRepository achievementRepository;

    @RequestMapping(value = "/achieve", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createAchievement(@RequestBody Achievement achievement) {
        String achievementId = achievementRepository.addAchievement(achievement);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", achievementId);
        return ResponseEntity.ok(jsonObject);
    }

    @RequestMapping(value = "/achieve/daily", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addDaily(@RequestParam String report, String userFromId) {
        String achievementId = achievementService.addDaily(report, userFromId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", achievementId);
        return ResponseEntity.ok(jsonObject);
    }
}
