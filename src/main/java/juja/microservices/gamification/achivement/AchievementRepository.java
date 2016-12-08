package juja.microservices.gamification.achivement;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AchievementRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    public String addAchievement(Achievement achievement) {
        mongoTemplate.save(achievement);
        return achievement.getId();
    }
    public List<UserAchievementDetails> getUserAchievementsDetails(List<String> userIds){
        List<UserAchievementDetails> resultList = new ArrayList<>();
        for (String userId : userIds) {
            List <AchievementDetail> details = getAllAchievementsByUserId(userId);
            resultList.add(new UserAchievementDetails(userId,details));
        }
        return resultList;
    }

    public List<AchievementDetail> getAllAchievementsByUserId(String id) {
        Query query = new Query(Criteria.where("userToId").is(id));
        List<Achievement> achievementsList = mongoTemplate.find(query, Achievement.class);
        List<AchievementDetail> result = new ArrayList<>();
        for (Achievement achievement:achievementsList) {
            result.add(new AchievementDetail(achievement));
        }
        return result;
    }
}
