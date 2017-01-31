package juja.microservices.gamification.dao;


import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserPointsSum;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AchievementRepository {
    private static final String COLLECTION_NAME = "achievement";

    @Inject
    private MongoTemplate mongoTemplate;

    public String addAchievement(Achievement achievement) {
        mongoTemplate.save(achievement);
        return achievement.getId();
    }

    public List<UserAchievementDetails> getUserAchievementsDetails(){
        List<String> userIds = getAllUserToIDs();
        List<UserAchievementDetails> resultList = new ArrayList<>();
        for (String userId : userIds) {
            List <Achievement> details = getAllAchievementsByUserId(userId);
            resultList.add(new UserAchievementDetails(userId,details));
        }
        return resultList;
    }

    public List<Achievement> getAllAchievementsByUserId(String id) {
        return mongoTemplate.find(new Query(Criteria.where("userToId").is(id)),Achievement.class);
    }

    public List<String> getAllUserToIDs(){
        return mongoTemplate.getCollection(COLLECTION_NAME).distinct("userToId");
    }

    public List<UserPointsSum> getAllUsersWithAchievement() {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.group("userToId")
                .first("userToId").as("userToId")
                .sum("pointCount").as("pointCount")
        );
        AggregationResults<UserPointsSum> result =
            mongoTemplate.aggregate(aggregation, Achievement.class, UserPointsSum.class);
        return result.getMappedResults();
    }
}
