package juja.microservices.gamification.achivement;

import java.util.List;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/achieve", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllUsersWithAchievement() {
        List users = achievementRepository.getAllUsersWithAchievement();

        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
