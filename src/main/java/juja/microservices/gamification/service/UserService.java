package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserPointsSum;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UserService {

    @Inject
    AchievementRepository repository;

    public List<UserAchievementDetails> getUserAchievementsDetails(UserIdsRequest ids) {
        return repository.getUserAchievementsDetails(ids);
    }

    public List<UserPointsSum> getAllUsersWithPointSum() {
        List<UserPointsSum> userPointsSums = repository.getAllUsersWithPointSum();
        if (userPointsSums.isEmpty()) {
            throw new IllegalStateException("User list with points is empty");
        }
        return userPointsSums;
    }
}
