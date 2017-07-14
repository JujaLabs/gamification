package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.KeeperDTO;
import juja.microservices.gamification.exceptions.KeepersMicroserviceExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.*;

/**
 * @author Vadim Dyachenko
 */
@Repository
public class RestKeeperRepository implements KeeperRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private RestTemplate restTemplate;

    @Value("${keepers.baseURL}")
    private String urlBase;
    @Value("${endpoint.keepers}")
    private String urlGetKeepers;

    @Override
    public List<KeeperDTO> getKeepers() {
        String urlTemplate = urlBase + urlGetKeepers;
        List<KeeperDTO> result;
        logger.debug("Send request to keeper repository");
        try {
            ResponseEntity<KeeperDTO[]> response = this.restTemplate.getForEntity(urlTemplate, KeeperDTO[].class);
            result = Arrays.asList(response.getBody());
        } catch (HttpClientErrorException ex) {
            throw new KeepersMicroserviceExchangeException("Keepers microservice Exchange Error: " + ex.getMessage());
        }
        logger.debug("Received list of active keeper: {}", result);
        return result;
    }
}