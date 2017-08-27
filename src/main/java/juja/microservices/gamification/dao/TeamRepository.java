package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.TeamDTO;

public interface TeamRepository {

    TeamDTO getTeamByUserUuid(String uuid);

}
