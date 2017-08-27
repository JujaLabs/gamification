package juja.microservices.gamification.dao.impl;

import juja.microservices.gamification.dao.TeamRepository;
import juja.microservices.gamification.entity.TeamDTO;
import juja.microservices.gamification.exceptions.TeamsMicroserviceExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

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
    @Value("${endpoint.teams.get.team.by.user.uuid}")
    private String urlGetTeamByUserUuid;

    @Override
    public TeamDTO getTeamByUserUuid(String uuid) {
        String urlTemplate = urlBase + urlGetTeamByUserUuid + uuid;
        TeamDTO result;
        logger.debug("Send request to team repository");
        try {
            ResponseEntity<TeamDTO> response = this.restTemplate.getForEntity(urlTemplate, TeamDTO.class);
            result = response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new TeamsMicroserviceExchangeException("Teams microservice Exchange Error: " + ex.getMessage());
        }
        logger.debug("Received active team : {}", result);
        return result;
    }
}