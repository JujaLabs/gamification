package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class AchievementRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    public String addAchievement(Achievement achievement) {
        if (achievement.getSendDate() == null) {
            achievement.setSendDate(getFormattedCurrentDate());
        }
        mongoTemplate.save(achievement);
        return achievement.getId();
    }

    public String getFormattedCurrentDate() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentDate);
    }

    public List<UserAchievementDetails> getUserAchievementsDetails(UserIdsRequest ids) {
        List<UserAchievementDetails> resultList = new ArrayList<>();
        for (String userId : ids.getToIds()) {
            List<Achievement> details = getAllAchievementsByUserToId(userId);
            resultList.add(new UserAchievementDetails(userId, details));
        }
        return resultList;
    }

    public List<Achievement> getAllAchievementsByUserToId(String to) {
        return mongoTemplate.find(new Query(Criteria.where("to").is(to)), Achievement.class);
    }

    public List<Achievement> getAllAchievementsByUserFromIdCurrentDateType(String from, AchievementType type) {
        String sendDate = getFormattedCurrentDate();

        return mongoTemplate.find(new Query(
                Criteria.where("from").is(from)
                        .and("sendDate").is(sendDate)
                        .and("type").is(type.toString())), Achievement.class);
    }

    public List<UserPointsSum> getAllUsersWithPointSum() {
        Aggregation aggregation = newAggregation(
                group("to")
                        .first("to").as("to")
                        .sum("point").as("point"),
                sort(Sort.Direction.ASC, "to")
        );
        AggregationResults<UserPointsSum> result =
                mongoTemplate.aggregate(aggregation, Achievement.class, UserPointsSum.class);
        return result.getMappedResults();
    }

    public List<Achievement> getAllCodenjoyAchievementsCurrentDate() {
        String sendDate = getFormattedCurrentDate();
        return mongoTemplate.find(new Query(
                Criteria.where("sendDate").is(sendDate)
                        .and("type").is(AchievementType.CODENJOY)), Achievement.class);
    }

    public List<Achievement> getWelcomeAchievementsByUser(String to) {
        return mongoTemplate.find(new Query(
                Criteria.where("to").is(to)
                        .and("type").is(AchievementType.WELCOME)), Achievement.class);
    }
}
