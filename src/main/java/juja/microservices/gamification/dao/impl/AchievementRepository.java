package juja.microservices.gamification.dao.impl;

import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

@Repository
@Slf4j
public class AchievementRepository {
    private final MongoTemplate mongoTemplate;

    @Value("${spring.data.mongodb.collection}")
    private String mongoCollectionName;

    public AchievementRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public String addAchievement(Achievement achievement) {
        if (achievement.getSendDate() == null) {
            LocalDateTime time = LocalDateTime.now();
            achievement.setSendDate(time);
        }
        mongoTemplate.save(achievement, mongoCollectionName);
        log.debug("Successfully added achievement to database. Type: {}, id: [{}]",
                achievement.getType(),
                achievement.getId());

        return achievement.getId();
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
        return mongoTemplate.find(new Query(Criteria.where("to").is(to)), Achievement.class, mongoCollectionName);
    }

    public List<Achievement> getAllAchievementsByUserFromIdCurrentDateType(String from, AchievementType type) {
        return mongoTemplate.find(
                new Query(
                        Criteria
                                .where("from").is(from)
                                .and("sendDate").gte(startCurrentDay())
                                .and("type").is(type.toString())),
                Achievement.class, mongoCollectionName);
    }

    public List<UserPointsSum> getAllUsersWithPointSum() {
        Aggregation aggregation = newAggregation(
                group("to")
                        .first("to").as("to")
                        .sum("point").as("point"),
                sort(Sort.Direction.ASC, "to")
        );
        AggregationResults<UserPointsSum> result = mongoTemplate
                .aggregate(
                        aggregation,
                        mongoCollectionName,
                        UserPointsSum.class);

        return result.getMappedResults();
    }

    public List<Achievement> getAllCodenjoyAchievementsCurrentDate() {
        return mongoTemplate
                .find(
                        new Query(
                                Criteria
                                        .where("sendDate").gte(startCurrentDay())
                                        .and("type").is(AchievementType.CODENJOY)),
                        Achievement.class,
                mongoCollectionName);
    }

    public List<Achievement> getAllThanksKeepersAchievementsCurrentWeek() {
        return mongoTemplate
                .find(
                        new Query(
                                Criteria
                                        .where("sendDate").gte(firstDayOfCurrentWeek())
                                        .and("type").is(AchievementType.THANKS_KEEPER)),
                        Achievement.class,
                        mongoCollectionName);
    }

    public List<Achievement> getWelcomeAchievementByUser(String to) {
        return mongoTemplate
                .find(
                        new Query(
                                Criteria
                                        .where("to").is(to)
                                        .and("type").is(AchievementType.WELCOME)),
                        Achievement.class,
                        mongoCollectionName);
    }

    public List<Achievement> getAllTeamAchievementsCurrentWeek(Set<String> uuids) {
        return mongoTemplate
                .find(
                        new Query(
                                Criteria
                                        .where("sendDate").gte(firstDayOfCurrentWeek())
                                        .and("type").is(AchievementType.TEAM)
                                        .and("to").in(uuids)),
                        Achievement.class,
                        mongoCollectionName);
    }

    private Date startCurrentDay() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date firstDayOfCurrentWeek() {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}