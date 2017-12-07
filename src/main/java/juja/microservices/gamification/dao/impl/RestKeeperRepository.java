package juja.microservices.gamification.dao.impl;

import feign.FeignException;
import juja.microservices.gamification.dao.KeeperClient;
import juja.microservices.gamification.dao.KeeperRepository;
import juja.microservices.gamification.entity.KeeperDTO;
import juja.microservices.gamification.exceptions.KeepersMicroserviceExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Repository
public class RestKeeperRepository implements KeeperRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KeeperClient keeperClient;

    public RestKeeperRepository(KeeperClient keeperClient) {
        this.keeperClient = keeperClient;
    }

    @Override
    public List<KeeperDTO> getKeepers() {
        List<KeeperDTO> result;

        logger.debug("Send request to keeper repository");
        try {
            result = keeperClient.getKeepers();
        } catch (FeignException ex) {
            throw new KeepersMicroserviceExchangeException("Keepers microservice Exchange Error: " + ex.getMessage());
        }
        logger.debug("Received list of active keeper: {}", result);

        return result;
    }
}