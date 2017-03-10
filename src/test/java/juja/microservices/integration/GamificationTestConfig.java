package juja.microservices.integration;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import javax.inject.Inject;

/**
 * @author danil.kuznetsov
 */
@TestConfiguration
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
