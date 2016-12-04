package juja.microservices.gamification.achievement;

import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.achivement.Achievement;
import juja.microservices.gamification.achivement.AchievementRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by danil.kuznetsov on 01/12/16.
 */
@RunWith(SpringRunner.class)
public class AchievementRepositoryIntegrationTest extends BaseIntegrationTest {

    @Inject
    private AchievementRepository achievementRepository;

    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json")
    @ShouldMatchDataSet(location = "/datasets/addNewAchievement-expected.json")
    public void shouldAddNewAchievementAndReturnNotNullId() {
        Achievement testAchievement = new Achievement("sasha", "ira", 2, "good work");

        String actualId = achievementRepository.addAchievement(testAchievement);
        assertThat(actualId,notNullValue());
    }
}
