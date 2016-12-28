package juja.microservices.gamification.achievement;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import juja.microservices.gamification.BaseIntegrationTest;
import juja.microservices.gamification.DAO.AchievementRepository;
import juja.microservices.gamification.Entity.Achievement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * Created by ВаНо on 28.12.2016.
 */
@RunWith(SpringRunner.class)
public class AchievementRepositorySpeedTest extends BaseIntegrationTest {
    private static boolean initialized = false;
    private static int numberOfRecords = 10_000; // Fongo's designed for lightweight testing.  100,000 items per collection max
    private long timeBorderInMillis = 5000;
    private String message = "Execution time more than " + timeBorderInMillis + " milliseconds";

    @Inject
    AchievementRepository achievementRepository;

    @Before
    @UsingDataSet(locations = "/datasets/selectAchievementById.json")
    public void setupDatabase() {
        if (!initialized) {
            for (int i = 0; i < numberOfRecords; i++) {
                Achievement testAchievement = new Achievement("sasha", "ira", 2, "good work");
                achievementRepository.addAchievement(testAchievement);
            }
        }
        initialized = true;
    }

    @Test
    public void getAllAchievementsByUserIdTestSpeed(){

        long time1 = System.currentTimeMillis();
        achievementRepository.getAllAchievementsByUserId("ira");
        long time2 = System.currentTimeMillis();
        long timeDifference = time2-time1;
        assertThat((message),timeDifference,lessThan(timeBorderInMillis));
    }

    @Test
    public void getAllAchievementsByUserIdAggregationTestSpeed(){

        long time1 = System.currentTimeMillis();
        achievementRepository.getAllAchievementsByUserIdAggregation("ira");
        long time2 = System.currentTimeMillis();
        long timeDifference = time2-time1;
        assertThat((message),timeDifference,lessThan(timeBorderInMillis));
    }

    @Test
    public void getAllAchievementsByUserIdSimpleWrappingTestSpeed(){

        long time1 = System.currentTimeMillis();
        achievementRepository.getAllAchievementsByUserIdSimpleWrapping("ira");
        long time2 = System.currentTimeMillis();
        long timeDifference = time2-time1;
        assertThat((message),timeDifference,lessThan(timeBorderInMillis));
    }
}
