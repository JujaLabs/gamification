package juja.microservices.gamification.service;

import juja.microservices.gamification.entity.Keeper;

import java.util.List;

public interface KeeperService {
    List<Keeper> getKeepers();
}
