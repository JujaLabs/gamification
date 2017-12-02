package juja.microservices.gamification.dao.impl;

import feign.FeignException;
import juja.microservices.gamification.dao.KeeperClient;
import juja.microservices.gamification.dao.KeeperRepository;
import juja.microservices.gamification.entity.KeeperDTO;
import juja.microservices.gamification.exceptions.KeepersMicroserviceExchangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vadim Dyachenko
 * @author Benjamin Novikov
 */

@Repository
@Slf4j
public class RestKeeperRepository implements KeeperRepository {
    private final KeeperClient keeperClient;

    public RestKeeperRepository(KeeperClient keeperClient) {
        this.keeperClient = keeperClient;
    }

    @Override
    public List<KeeperDTO> getKeepers() {
        List<KeeperDTO> result;

        log.debug("Keepers service request has been sent");
        try {
            result = keeperClient.getKeepers();
            log.debug("Request to Keepers service has finished");
        } catch (FeignException ex) {
            throw new KeepersMicroserviceExchangeException("Keepers microservice Exchange Error: " + ex.getMessage());
        }
        log.info("Received list of active keepers: {}", result);

        return result;
    }
}
