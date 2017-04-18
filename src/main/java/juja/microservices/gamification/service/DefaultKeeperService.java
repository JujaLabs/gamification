package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.KeeperRepository;
import juja.microservices.gamification.entity.Keeper;

import javax.inject.Inject;
import java.util.List;

public class DefaultKeeperService implements KeeperService {

    private final KeeperRepository keeperRepository;

    @Inject
    public DefaultKeeperService(KeeperRepository keeperRepository) {
        this.keeperRepository = keeperRepository;
    }

    @Override
    public List<Keeper> getKeepers() {
        return keeperRepository.getKeepers();
    }
}