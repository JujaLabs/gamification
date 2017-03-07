package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource;

/**
 * @author Danil Kuznetsov
 */
@RunWith(SpringRunner.class)
public class UserControllerAcceptanceTest extends BaseAcceptanceTest {

    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testUrlAchieveSum() throws IOException {

        //given
        String url = "/user/pointSum";
        String expectedResponse = convertToString(resource("expectedJson/responsePointSum.json"));

        //when
        Response actualResponse =
                given()
                        .contentType("application/json")
                        .when()
                        .get(url)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        printConsoleReport(url, expectedResponse, actualResponse.body());

        //then
        assertJsonEquals(actualResponse.asString(), expectedResponse);


    }


}
