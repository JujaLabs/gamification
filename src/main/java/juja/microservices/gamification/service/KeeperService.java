package juja.microservices.gamification.service;

import juja.microservices.gamification.entity.Keeper;

import java.util.List;
import java.util.Map;

public interface KeeperService {
    Map<Keeper, List<String>> getKeepers();
}
