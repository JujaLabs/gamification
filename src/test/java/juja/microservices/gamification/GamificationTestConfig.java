package juja.microservices.gamification;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import javax.inject.Inject;

/**
 * Created by danil.kuznetsov on 01/12/16.
 */
@Configuration
public class GamificationTestConfig extends AbstractMongoConfiguration {

    public static final String TEST_DATABASE_NAME = "gamification-test";

    @Inject
    private MongoClient mongoClient;

    @Bean
    public MongoClient mongoClient() {
        return new Fongo("inMemoryMongoClient").getMongo();
    }


    @Override
    protected String getDatabaseName() {
        return TEST_DATABASE_NAME;
    }

    @Bean
    @Override
    public MongoClient mongo() {
        return mongoClient;
    }

}
