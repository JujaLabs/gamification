package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class AchievementRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private MongoTemplate mongoTemplate;

    public String addAchievement(Achievement achievement) {

        if (achievement.getSendDate() == null) {
            LocalDateTime time = LocalDateTime.now();
            achievement.setSendDate(time);
            logger.debug("Set send date [{}] to achievement", time);
        }
        logger.debug("Save achievement to database");
        mongoTemplate.save(achievement);
        logger.debug("Received id from database [{}]", achievement.getId());
        return achievement.getId();
    }

    public List<UserAchievementDetails> getUserAchievementsDetails(UserIdsRequest ids) {
        List<UserAchievementDetails> resultList = new ArrayList<>();
        logger.debug("Request data from database");
        for (String userId : ids.getToIds()) {
            List<Achievement> details = getAllAchievementsByUserToId(userId);
            resultList.add(new UserAchievementDetails(userId, details));
        }
        logger.debug("Received data from database");
        return resultList;
    }

    public List<Achievement> getAllAchievementsByUserToId(String to) {
        return mongoTemplate.find(new Query(Criteria.where("to").is(to)), Achievement.class);
    }

    public List<Achievement> getAllAchievementsByUserFromIdCurrentDateType(String from, AchievementType type) {
        return mongoTemplate.find(new Query(
                Criteria.where("from").is(from)
                        .and("sendDate").gte(startCurrentDay())
                        .and("type").is(type.toString())), Achievement.class);
    }

    public List<UserPointsSum> getAllUsersWithPointSum() {
        Aggregation aggregation = newAggregation(
                group("to")
                        .first("to").as("to")
                        .sum("point").as("point"),
                sort(Sort.Direction.ASC, "to")
        );
        logger.debug("Request data from database");
        AggregationResults<UserPointsSum> result =
                mongoTemplate.aggregate(aggregation, Achievement.class, UserPointsSum.class);
        logger.debug("Received data from database");
        return result.getMappedResults();
    }

    public List<Achievement> getAllCodenjoyAchievementsCurrentDate() {
        return mongoTemplate.find(new Query(
                Criteria.where("sendDate").gte(startCurrentDay())
                        .and("type").is(AchievementType.CODENJOY)), Achievement.class);
    }

    public List<Achievement> getAllThanksKeepersAchievementsCurrentWeek() {
        return mongoTemplate.find(new Query(
                Criteria.where("sendDate").gte(firstDayOfCurrentWeek())
                        .and("type").is(AchievementType.THANKS_KEEPER)), Achievement.class);
    }

    public List<Achievement> getWelcomeAchievementByUser(String to) {
        return mongoTemplate.find(new Query(
                Criteria.where("to").is(to)
                        .and("type").is(AchievementType.WELCOME)), Achievement.class);
    }

    private Date startCurrentDay() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date firstDayOfCurrentWeek() {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}