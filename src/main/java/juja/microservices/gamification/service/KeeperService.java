package juja.microservices.gamification.service;

import juja.microservices.gamification.entity.KeeperDTO;

import java.util.List;

/*
 * @author Benjamin Novikov, November 2017, task #
 */
public interface KeeperService {
    List<KeeperDTO> getKeepers();
}
