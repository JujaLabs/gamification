package juja.microservices.gamification.service;

import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;

import java.util.List;

/*
 * @author Benjamin Novikov
 */
public interface UserService {
    List<UserAchievementDetails> getUserAchievementsDetails(UserIdsRequest ids);
    List<UserPointsSum> getAllUsersWithPointSum();
}
