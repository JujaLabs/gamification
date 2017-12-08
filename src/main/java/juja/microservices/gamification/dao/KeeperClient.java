package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.KeeperDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/*
 * @author Benjamin Novikov
 */
@FeignClient(name = "gateway")
public interface KeeperClient {
    @RequestMapping(method = RequestMethod.GET, value = "/v1/keepers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<KeeperDTO> getKeepers();
}
