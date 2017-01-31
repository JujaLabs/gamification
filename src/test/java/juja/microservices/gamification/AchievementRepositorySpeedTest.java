package juja.microservices.gamification;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.integration.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * @author ВаНо
 */
@RunWith(SpringRunner.class)
public class AchievementRepositorySpeedTest extends BaseIntegrationTest {
    private static boolean initialized = false;
    private static final int numberOfRecords = 10_000; // Fongo was designed for lightweight testing.  100,000 items per collection max
    private final long  timeBorderInMillis = 5000;
    private final String message = "Execution time more than " + timeBorderInMillis + " milliseconds";

    @Inject
    AchievementRepository achievementRepository;

    @Before
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void setupDatabase() {
        if (!initialized) {
            for (int i = 0; i < numberOfRecords; i++) {
                Achievement testAchievement = new Achievement("sasha", "ira", 2, "good work", AchievementType.DAILY);
                achievementRepository.addAchievement(testAchievement);
            }
        }
        initialized = true;
    }

    @Test
    public void getAllAchievementsByUserIdTestSpeed(){

        long startTime = System.currentTimeMillis();
        achievementRepository.getAllAchievementsByUserId("ira");
        long endTime = System.currentTimeMillis();
        long timeDifference = endTime - startTime;
        assertThat((message),timeDifference,lessThan(timeBorderInMillis));
    }
}
