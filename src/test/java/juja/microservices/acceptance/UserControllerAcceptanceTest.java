package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import juja.microservices.gamification.Gamification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder.mongoDb;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author danil.kuznetsov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {Gamification.class})
@DirtiesContext
public class UserControllerAcceptanceTest {

    @LocalServerPort
    int localPort;

    @Rule
    public MongoDbRule mongoDbRule = new MongoDbRule(
            mongoDb()
                    .databaseName("gamification")
                    .host("127.0.0.1")
                    .port(27017)
                    .build()
    );

    @Before
    public void setup() {
        RestAssured.port = localPort;
        RestAssured.baseURI = "http://localhost";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testUrlAchieveSum() {
        Response result = given()
                .contentType("application/json")
                .when()
                .get("/user/pointSum")
                .then()
                .statusCode(200)
                .assertThat().body(matchesJsonSchemaInClasspath("jsonSchema/responsePointSum.json"))
                .extract()
                .response();

        System.out.println("\n\nResponse for /user/pointSum");
        System.out.println("\n\n"+result.asString()+"\n\n");
    }
}
