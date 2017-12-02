package juja.microservices.gamification.service.impl;

import juja.microservices.gamification.dao.AchievementRepository;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;
import juja.microservices.gamification.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final AchievementRepository repository;

    public UserServiceImpl(AchievementRepository repository) {
        this.repository = repository;
    }

    public List<UserAchievementDetails> getUserAchievementsDetails(UserIdsRequest ids) {
        log.debug("Send request to repository: get users achievement detail");
        return repository.getUserAchievementsDetails(ids);
    }

    public List<UserPointsSum> getAllUsersWithPointSum() {
        log.debug("Send request to repository: get all users with point sum");
        List<UserPointsSum> userPointsSums = repository.getAllUsersWithPointSum();

        if (userPointsSums.isEmpty()) {
            log.warn("User list with points is empty");
            throw new IllegalStateException("User list with points is empty");
        }

        return userPointsSums;
    }
}
