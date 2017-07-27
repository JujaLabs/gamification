package juja.microservices.gamification.dao.impl;

import juja.microservices.gamification.dao.TeamRepository;
import juja.microservices.gamification.entity.Team;
import juja.microservices.gamification.exceptions.KeepersMicroserviceExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Petro Kramar
 */
@Repository
public class RestTeamRepository implements TeamRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private RestTemplate restTemplate;

    @Value("${teams.baseURL}")
    private String urlBase;
    @Value("${endpoint.teams}")
    private String urlGetKeepers;

    @Override
    public Team getTeamByUuid(String uuid) {
        String urlTemplate = urlBase + urlGetKeepers + "/{" + uuid + "}";
        Team result;
        logger.debug("Send request to keeper repository");
        try {
            ResponseEntity<Team> response = this.restTemplate.getForEntity(urlTemplate, Team.class);
            result = response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new KeepersMicroserviceExchangeException("Teams microservice Exchange Error: " + ex.getMessage());
        }
        logger.debug("Received active team : {}", result);
        return result;
    }
}