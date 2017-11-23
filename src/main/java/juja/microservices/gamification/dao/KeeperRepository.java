package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.KeeperDTO;

import java.util.List;

/**
 * @author Vadim Dyachenko
 */
public interface KeeperRepository {
    List<KeeperDTO> getKeepers();
}
