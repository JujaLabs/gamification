package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.TeamRepository;
import juja.microservices.gamification.entity.TeamDTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class TeamService {
    @Inject
    private TeamRepository teamRepository;

    public TeamDTO getTeamByUuid(String uuid) {
        return teamRepository.getTeamByUuid(uuid);
    }
}