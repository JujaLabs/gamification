package juja.microservices.gamification.achivement;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> sendAchievement(@RequestBody Achievement achievement) {
        String achievementId = achievementRepository.addAchievement(achievement);
        return ResponseEntity.ok(achievementId);
    }
}
