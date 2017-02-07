package juja.microservices.gamification.Service;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.service.AchievementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ВаНо on 07.02.2017.
 */
@RunWith(SpringRunner.class)
public class AchievementServiceTest extends BaseIntegrationTest{

    @Inject
    private AchievementRepository achievementRepository;

    @Inject
    private AchievementService achievementService;

    @Test
    @UsingDataSet(locations = "/datasets/addDailyAchievement.json")
    public void shouldAddNewDailyAchievement(){
        String shouldMuchDescription = "This is a daily report";
        String userToId = "oleg";
        achievementService.addDaily(shouldMuchDescription,userToId);
        List <Achievement> achievementList =  achievementRepository.getAllAchievementsByUserToId("oleg");
        String actualDescription = achievementList.get(0).getDescription();
        assertEquals(shouldMuchDescription,actualDescription);
    }
    @Test
    @UsingDataSet(locations = "/datasets/addDailyAchievement.json")
    public void shouldUpdateExistingDailyAchievement(){
        String userToId = "oleg";
        String userFromId = userToId;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        String firstDescription = "This is a daily report";
        String updateForDescription = "Some update for the daily report";


        Achievement testAchievement = new Achievement(userFromId,userToId,1,firstDescription, AchievementType.DAILY);
        testAchievement.setSendDate(currentDate);
        achievementRepository.addAchievement(testAchievement);
        achievementService.addDaily(updateForDescription,userToId);
        String shouldMuchDescription = firstDescription
                .concat(System.lineSeparator())
                .concat(updateForDescription);
        List <Achievement> achievementList =  achievementRepository.getAllAchievementsByUserToId("oleg");
        assertEquals(shouldMuchDescription,achievementList.get(0).getDescription());
    }
}
