package juja.microservices.gamification.achivement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class AchievementRepository {
    private static final String COLLECTION_NAME = "achievement";

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
    public List<UserAchievementDetails> getUserAchievementsDetails(){
        List<String> userIds = getAllUserToIDs(COLLECTION_NAME);
        List<UserAchievementDetails> resultList = new ArrayList<>();
        for (String userId : userIds) {
            List <AchievementDetail> details = getAllAchievementsByUserId(userId);
            resultList.add(new UserAchievementDetails(userId,details));
        }
        return resultList;
    }

    public List<AchievementDetail> getAllAchievementsByUserId(String id) {
        List<AchievementDetail> result = new ArrayList<>();
        DBCollection collection = mongoTemplate.getCollection(COLLECTION_NAME);
        BasicDBObject query = new BasicDBObject("userToId",id);
        BasicDBObject field = new BasicDBObject();

        field.put("userFromId",1);
        field.put("description",1);
        field.put("pointCount",1);
        field.put("_id",1);

        DBCursor cursor = collection.find(query,field);
        while (cursor.hasNext()){
            BasicDBObject object = (BasicDBObject) cursor.next();
            String userFromId = object.getString("userFromId");
            String description = object.getString("description");
            int pointCount = object.getInt("pointCount");
            String sendDate = object.getString("_id");
            AchievementDetail achievementDetail =
                    new AchievementDetail(userFromId,sendDate,description,pointCount);
            result.add(achievementDetail);
        }
        return result;
    }
    public List <String> getAllUserToIDs(String collectionName){
        Set<String> resultSet = new HashSet<>();
        DBCollection collection = mongoTemplate.getCollection(collectionName);
        BasicDBObject field = new BasicDBObject();
        field.put("userToId",1);
        DBCursor cursor = collection.find(new BasicDBObject(),field);
        while (cursor.hasNext()){
            BasicDBObject object = (BasicDBObject) cursor.next();
            resultSet.add(object.getString("userToId"));
        }
        List <String> resultList = new ArrayList<>();
        resultList.addAll(resultSet);
        return resultList;
    }
}
