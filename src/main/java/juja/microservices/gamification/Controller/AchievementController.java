package juja.microservices.gamification.Controller;

import juja.microservices.gamification.DAO.AchievementRepository;
import juja.microservices.gamification.Entity.Achievement;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;

@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class AchievementController {

    @Inject
    private AchievementRepository achievementRepository;

    @RequestMapping(value = "/achieve", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createAchievement(@RequestBody Achievement achievement) {
        String achievementId = achievementRepository.addAchievement(achievement);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",achievementId);
        return ResponseEntity.ok(jsonObject);
    }
}
