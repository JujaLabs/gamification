package juja.microservices.gamification.achivement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AchievementRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    public String addAchievement(Achievement achievement) {
        mongoTemplate.save(achievement);
        return achievement.getId();
    }

    public List<UserPointsSum> getAllUsersWithAchievement() {
        List<UserPointsSum> result = new ArrayList<>();
        DBCollection collection = mongoTemplate.getCollection("achievement");

        BasicDBObject query = new BasicDBObject();
        BasicDBObject field = new BasicDBObject();

        field.put("userToId", 1);
        field.put("pointCount", 1);

        DBCursor cursor = collection.find(query, field);
        Map<String, Integer> map = new HashMap<>();
        while (cursor.hasNext()) {
            BasicDBObject object = (BasicDBObject) cursor.next();
            String userToId = object.getString("userToId");
            int pointCount = object.getInt("pointCount");

            if (map.containsKey(userToId)) {
                map.put(userToId, map.get(userToId) + pointCount);
            } else {
                map.put(userToId, pointCount);
            }
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String user = entry.getKey().toString();
            Integer pointSum = entry.getValue();
            UserPointsSum userPointsSum = new UserPointsSum(user, pointSum);
            result.add(userPointsSum);
        }
        return result;
    }
}
