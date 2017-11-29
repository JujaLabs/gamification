package juja.microservices.gamification.service.impl;

import juja.microservices.gamification.dao.KeeperRepository;
import juja.microservices.gamification.entity.KeeperDTO;
import juja.microservices.gamification.service.KeeperService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Service
public class KeeperServiceImpl implements KeeperService {
    private final KeeperRepository keeperRepository;

    public KeeperServiceImpl(KeeperRepository keeperRepository) {
        this.keeperRepository = keeperRepository;
    }

    public List<KeeperDTO> getKeepers() {
        return keeperRepository.getKeepers();
    }
}