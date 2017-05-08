package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.Keeper;

import java.util.List;

public interface KeeperRepository {
    List<Keeper> getKeepers();
}
