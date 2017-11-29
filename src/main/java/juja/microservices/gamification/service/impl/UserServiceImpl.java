package juja.microservices.gamification.service.impl;

import juja.microservices.gamification.dao.impl.AchievementRepository;
import juja.microservices.gamification.entity.UserAchievementDetails;
import juja.microservices.gamification.entity.UserIdsRequest;
import juja.microservices.gamification.entity.UserPointsSum;
import juja.microservices.gamification.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AchievementRepository repository;

    public UserServiceImpl(AchievementRepository repository) {
        this.repository = repository;
    }

    public List<UserAchievementDetails> getUserAchievementsDetails(UserIdsRequest ids) {
        logger.debug("Send request to repository: get users achievement detail");
        return repository.getUserAchievementsDetails(ids);
    }

    public List<UserPointsSum> getAllUsersWithPointSum() {
        logger.debug("Send request to repository: get all users with point sum");
        List<UserPointsSum> userPointsSums = repository.getAllUsersWithPointSum();
        if (userPointsSums.isEmpty()) {
            logger.warn("User list with points is empty");
            throw new IllegalStateException("User list with points is empty");
        }
        return userPointsSums;
    }
}
