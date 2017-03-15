package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Option;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

@RunWith(SpringRunner.class)
public class AchievementAcceptanceTest extends BaseAcceptanceTest {

    private static final String ONE_ACHIEVEMENT_ID = "achievement id";
    private static final String TWO_ACHIEVEMENT_ID = "[achievement1 id, achievement2 id]";
    private static final String THREE_ACHIEVEMENT_ID = "[ achievement1 id, achievement2 id, achievement3 id]";

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddDailyReport() throws IOException {

        //given
        String url = ACHIEVE_DAILY_URL;
        String jsonContentRequest = convertToString(resource("datasets/addDailyReport.json"));
        String jsonContentControlResponce = convertToString(
                resource("expectedJson/responseUserPointSumDailyOrThanks.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
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
        String url = ACHIEVE_THANKS_URL;
        String jsonContentRequest = convertToString(resource("datasets/addFirstThanks.json"));
        String jsonContentControlResponce = convertToString(resource(
                "expectedJson/responseUserPointSumDailyOrThanks.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
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
        String url = ACHIEVE_THANKS_URL;
        String jsonFirstContentRequest = convertToString(resource("datasets/addFirstThanks.json"));
        String jsonSecondContentRequest = convertToString(resource("datasets/addSecondThanks.json"));
        String jsonContentControlResponce = convertToString(resource(
                "expectedJson/responseUserPointSumTwoThanks.json"));

        //when
        Response actualResponse = getResponse(url, jsonFirstContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
        actualResponse = getResponse(url, jsonSecondContentRequest, HttpMethod.POST);
        printConsoleReport(url, TWO_ACHIEVEMENT_ID, actualResponse.body());
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
        String url = ACHIEVE_CODENJOY_URL;
        String jsonContentRequest = convertToString(resource("datasets/addCodenjoy.json"));
        String jsonContentControlResponce = convertToString(resource("expectedJson/responseUserPointSumCodenjoy.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, THREE_ACHIEVEMENT_ID, actualResponse.body());
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
        String url = ACHIEVE_INTERVIEW_URL;
        String jsonContentRequest = convertToString(resource("datasets/addInterview.json"));
        String jsonContentControlResponce = convertToString(resource(
                "expectedJson/responseUserPointSumInterview.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponce);
    }
}
