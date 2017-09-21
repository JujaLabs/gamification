package juja.microservices.gamification.service;

import juja.microservices.gamification.dao.KeeperRepository;
import juja.microservices.gamification.entity.KeeperDTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Vadim Dyachenko
 */
@Service
public class KeeperService {
    @Inject
    private KeeperRepository keeperRepository;

    public List<KeeperDTO> getKeepers() {
        return keeperRepository.getKeepers();
    }
}