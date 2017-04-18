package juja.microservices.gamification.dao;

import juja.microservices.gamification.entity.Keeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.*;

public class RestKeeperRepository implements KeeperRepository {

    private final RestTemplate restTemplate;

    @Value("${user.baseURL}")
    private String urlBase;
    @Value("${endpoint.keepers}")
    private String urlGetKeepers;

    @Inject
    public RestKeeperRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public List<Keeper> getKeepers() {
        String urlTemplate = urlBase + urlGetKeepers + "?param=uuid,keepersOf,from";
        List<Keeper> result = new ArrayList<>();
        try {
            ResponseEntity<Keeper[]> response = this.restTemplate.getForEntity(urlTemplate, Keeper[].class);
            result = Arrays.asList(response.getBody());
        } catch (HttpClientErrorException ex) {
            //TODO implement catch exception body
        }
        return result;
    }
}
