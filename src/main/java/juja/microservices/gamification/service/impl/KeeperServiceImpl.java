package juja.microservices.gamification.service.impl;

import juja.microservices.gamification.dao.KeeperRepository;
import juja.microservices.gamification.entity.KeeperDTO;
import juja.microservices.gamification.service.KeeperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vadim Dyachenko
 * @author Benjamin Novikov
 */

@Service
@Slf4j
public class KeeperServiceImpl implements KeeperService {
    private final KeeperRepository repository;

    public KeeperServiceImpl(KeeperRepository repository) {
        this.repository = repository;
    }

    public List<KeeperDTO> getKeepers() {
        log.debug("Send request to repository: get keepers");

        return repository.getKeepers();
    }
}