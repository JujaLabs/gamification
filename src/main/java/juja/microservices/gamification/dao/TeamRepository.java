package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.Team;

public interface TeamRepository {

    Team getTeamByUuid(String uuid);

}
