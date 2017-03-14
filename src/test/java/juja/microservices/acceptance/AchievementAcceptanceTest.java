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
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

@RunWith(SpringRunner.class)
public class AchievementAcceptanceTest extends BaseAcceptanceTest {

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddDailyReport() throws IOException {

        //given
        String url = "/achieve/daily";
        String jsonContentRequest = "{\"from\":\"max\",\"description\":\"Daily report\"}";
        String jsonContentControlResponce = "[{\"to\":\"max\",\"point\":1}]";

        //when
        Response actualResponse = getActualResponse(url, jsonContentRequest);
        printConsoleReport(url, "achievement id", actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponce);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddThanks() throws IOException {

        //given
        String url = "/achieve/thanks";
        String jsonContentRequest = "{\"from\":\"john\",\"to\":\"max\",\"description\":\"Thanks for help\"}";
        String jsonContentControlResponce = "[{\"to\":\"max\",\"point\":1}]";

        //when
        Response actualResponse = getActualResponse(url, jsonContentRequest);
        printConsoleReport(url, "achievement id", actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponce);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddTwoThanks() throws IOException {

        //given
        String url = "/achieve/thanks";
        String jsonFirstContentRequest = "{\"from\":\"john\",\"to\":\"max\",\"description\":\"Thanks for help\"}";
        String jsonSecondContentRequest = "{\"from\":\"john\",\"to\":\"bill\",\"description\":\"Great solution\"}";
        String jsonContentControlResponce = "[{\"to\":\"max\",\"point\":1},{\"to\":\"bill\",\"point\":1}," +
                "{\"to\":\"john\",\"point\":1}]";

        //when
        Response actualResponse = getActualResponse(url, jsonFirstContentRequest);
        printConsoleReport(url, "achievement id", actualResponse.body());
        actualResponse = getActualResponse(url, jsonSecondContentRequest);
        printConsoleReport(url, "[achievement1 id, achievement2 id]", actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponce);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testCodenjoy() throws IOException {

        //given
        String url = "/achieve/codenjoy";
        String jsonContentRequest = "{\"from\":\"bill\",\"firstPlace\":\"max\",\"secondPlace\":\"john\"," +
                "\"thirdPlace\":\"bob\"}";
        String jsonContentControlResponce = "[{\"to\":\"bob\",\"point\":1}, {\"to\":\"john\",\"point\":3}, " +
                "{\"to\":\"max\",\"point\":5}]";

        //when
        Response actualResponse = getActualResponse(url, jsonContentRequest);
        printConsoleReport(url, "[ achievement1 id, achievement2 id, achievement3 id]", actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponce);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddInterview() throws IOException {

        //given
        String url = "/achieve/interview";
        String jsonContentRequest = "{\"from\":\"max\",\"description\":\"Interview\"}";
        String jsonContentControlResponce = "[{\"to\":\"max\",\"point\":10}]";

        //when
        Response actualResponse = getActualResponse(url, jsonContentRequest);
        printConsoleReport(url, "achievement id", actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponce);
    }

    private Response getActualResponse(String url, String jsonContentRequest) {
        return given()
                .contentType("application/json")
                .body(jsonContentRequest)
                .when()
                .post(url)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    private Response getControlResponse() {
        return given()
                .contentType("application/json")
                .body("")
                .when()
                .get("/user/pointSum")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
