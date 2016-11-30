package juja.microservices.gamification;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class Gamification {

    @Value("spring.data.mongodb.host")
    private String host;
    @Value("spring.data.mongodb.database")
    private String database;

    public static void main(String[] args) {
        SpringApplication.run(Gamification.class, args);
    }

    @Bean
    public Mongo mongo() {
        return new MongoClient(host);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), database);
    }
}
