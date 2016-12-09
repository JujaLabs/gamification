package juja.microservices.gamification.achivement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List <String> getAllUserToNames(String collectionName){
        Set<String> resultSet = new HashSet<>();
        DBCollection collection = mongoTemplate.getCollection(collectionName);
        BasicDBObject query = new BasicDBObject();
        query.put("userToId",1);
        DBCursor cursor = collection.find(new BasicDBObject(),query);
        while (cursor.hasNext()){
            BasicDBObject object = (BasicDBObject) cursor.next();
            resultSet.add(object.getString("userToId"));
        }
        List <String> resultList = new ArrayList<>();
        resultList.addAll(resultSet);
        return resultList;
    }
}
