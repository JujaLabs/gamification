package juja.microservices.gamification.service;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author BaHo
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
        String firstDescription = "This is a daily report";
        String updateForDescription = "Some update for the daily report";
        String shouldMuchDescription = firstDescription
                .concat(System.lineSeparator())
                .concat(updateForDescription);


        Achievement testAchievement = new Achievement(userFromId,userToId,1,firstDescription, AchievementType.DAILY);
        achievementRepository.addAchievement(testAchievement);

        achievementService.addDaily(updateForDescription,userToId);
        List <Achievement> achievementList =  achievementRepository.getAllAchievementsByUserToId("oleg");
        String actualDescription = achievementList.get(0).getDescription();

        assertEquals(shouldMuchDescription,actualDescription);
    }
    @Test
    @UsingDataSet(locations = "/datasets/addDailyAchievement.json")
    public void shouldUpdateOnlyDescriptionFieldDailyAchievement(){
        String userToId = "oleg";
        String userFromId = userToId;
        String firstDescription = "This is a daily report";
        String updateForDescription = "Some update for the daily report";
        String shouldMuchDescription = firstDescription
                .concat(System.lineSeparator())
                .concat(updateForDescription);


        Achievement testAchievement = new Achievement(userFromId,userToId,1,firstDescription, AchievementType.DAILY);
        String achievementId = achievementRepository.addAchievement(testAchievement);
        int achievementPointCount = testAchievement.getPointCount();
        List <Achievement> achievementListBeforeUpdate =  achievementRepository.getAllAchievementsByUserToId("oleg");
        Achievement achievementFromDbBeforeUpdate = achievementListBeforeUpdate.get(0);
        String sendDateBeforeUpdate = achievementFromDbBeforeUpdate.getSendDate();
        String typeBeforeUpdate = achievementFromDbBeforeUpdate.getType().toString();

        achievementService.addDaily(updateForDescription,userToId);
        List <Achievement> achievementList =  achievementRepository.getAllAchievementsByUserToId("oleg");
        Achievement updatedAchievement = achievementList.get(0);
        String sendDateAfterUpdate = updatedAchievement.getSendDate();
        String actualDescription = updatedAchievement.getDescription();

        assertEquals(achievementId,updatedAchievement.getId());
        assertEquals(userToId,updatedAchievement.getUserToId());
        assertEquals(achievementPointCount,updatedAchievement.getPointCount());
        assertEquals(userFromId,updatedAchievement.getUserFromId());
        assertEquals(shouldMuchDescription,actualDescription);
        assertEquals(sendDateBeforeUpdate,sendDateAfterUpdate);
        assertEquals(typeBeforeUpdate,updatedAchievement.getType().toString());

    }
}
