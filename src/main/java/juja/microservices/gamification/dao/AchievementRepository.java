package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.Achievement;
import juja.microservices.gamification.entity.AchievementType;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;

import java.util.List;
import java.util.Set;

/*
 * @author Benjamin Novikov
 */

public interface AchievementRepository {
    public String addAchievement(Achievement achievement);
    public List<UserAchievementDetails> getUserAchievementsDetails(UserIdsRequest ids);
    public List<Achievement> getAllAchievementsByUserToId(String to);
    public List<Achievement> getAllAchievementsByUserFromIdCurrentDateType(String from, AchievementType type);
    public List<UserPointsSum> getAllUsersWithPointSum();
    public List<Achievement> getAllCodenjoyAchievementsCurrentDate();
    public List<Achievement> getAllThanksKeepersAchievementsCurrentWeek();
    public List<Achievement> getWelcomeAchievementByUser(String to);
    public List<Achievement> getAllTeamAchievementsCurrentWeek(Set<String> uuids);
}