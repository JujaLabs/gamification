package juja.microservices.gamification.controller;

import java.util.List;
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

    @RequestMapping(value = "/achieve/daily", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addDaily(@RequestParam String report, String userFromId) {
        String achievementId = achievementService.addDaily(report, userFromId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", achievementId);
        return ResponseEntity.ok(jsonObject);
    }

    @RequestMapping(value = "/achieve/thanks", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addThanks(@RequestParam String userFromId, String userToId, String report) {
        List<String> achievementId = achievementService.addThanks(userFromId, userToId, report);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", achievementId);
        return ResponseEntity.ok(jsonObject);
    }
}
