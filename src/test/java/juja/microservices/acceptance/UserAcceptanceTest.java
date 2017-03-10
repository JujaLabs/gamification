package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

/**
 * @author Danil Kuznetsov
 */
@RunWith(SpringRunner.class)
public class UserAcceptanceTest extends BaseAcceptanceTest {

    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testUrlPointSum() throws IOException {

        //given
        String url = "/user/pointSum";
        String expectedResponse = convertToString(resource("expectedJson/responseUserPointSum.json"));

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
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }

    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testUrlAchieveDetails() throws IOException {

        //given
        String url = "/user/achieveDetails";
        String expectedResponse = convertToString(resource("expectedJson/responseUserAchieveDetails.json"));

        String jsonContentRequest = "{\"toIds\":[\"max\",\"peter\"]}";

        //when
        Response actualResponse =
                given()
                        .contentType("application/json")
                        .body(jsonContentRequest)
                        .when()
                        .post(url)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        printConsoleReport(url, expectedResponse, actualResponse.body());

        //then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }
}
