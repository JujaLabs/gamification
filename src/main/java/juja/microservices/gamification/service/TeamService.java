package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.TeamRepository;
import juja.microservices.gamification.entity.Team;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class TeamService {
    @Inject
    private TeamRepository teamRepository;

    public Team getTeamByUuid(String uuid) {
        return teamRepository.getTeamByUuid(uuid);
    }
}