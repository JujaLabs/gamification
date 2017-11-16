package juja.microservices.integration;

import com.github.fakemongo.Fongo;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfiguration;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.SpringMongoDbRule;
import com.mongodb.MockMongoClient;
import com.mongodb.MongoClient;
import juja.microservices.gamification.Gamification;
import org.junit.Rule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static juja.microservices.integration.GamificationTestConfig.TEST_DATABASE_NAME;

/**
 * @author danil.kuznetsov
 */
@SpringBootTest(classes = {GamificationTestConfig.class, Gamification.class})
public class BaseIntegrationTest {

    @Rule
    public MongoDbRule mongoDbRule = getSpringFongoMongoDbRule();

    @Inject
    protected WebApplicationContext webApplicationContext;

    public static SpringMongoDbRule getSpringFongoMongoDbRule() {
        MongoDbConfiguration mongoDbConfiguration = new MongoDbConfiguration();
        mongoDbConfiguration.setDatabaseName(TEST_DATABASE_NAME);
        MongoClient mongo = MockMongoClient.create(new Fongo("this-fongo-instance-is-ignored"));
        mongoDbConfiguration.setMongo(mongo);
        return new SpringMongoDbRule(mongoDbConfiguration);
    }
}
