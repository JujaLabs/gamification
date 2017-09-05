package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Option;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AchievementAcceptanceTest extends BaseAcceptanceTest {

    private static final String ACHIEVE_DAILY_URL = "/v1/gamification/achieve/daily";
    private static final String ACHIEVE_THANKS_URL = "/v1/gamification/achieve/thanks";
    private static final String ACHIEVE_CODENJOY_URL = "/v1/gamification/achieve/codenjoy";
    private static final String ACHIEVE_INTERVIEW_URL = "/v1/gamification/achieve/interview";
    private static final String ACHIEVE_WELCOME_URL = "/v1/gamification/achieve/welcome";
    private static final String ACHIEVE_TEAM_URL = "/v1/gamification/achieve/team";
    private static final String USER_POINT_SUM_URL = "/v1/gamification/user/pointSum";
    private static final String EMPTY_JSON_CONTENT_REQUEST = "";
    private static final String ONE_ACHIEVEMENT_ID = "achievement id";
    private static final String TWO_ACHIEVEMENT_ID = "[achievement1 id, achievement2 id]";
    private static final String THREE_ACHIEVEMENT_ID = "[ achievement1 id, achievement2 id, achievement3 id]";
    private static final String FOUR_ACHIEVEMENT_ID = "[ achievement1 id, achievement2 id, achievement3 id, " +
            "achievement4 id]";

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddDailyReport() throws IOException {

        //given
        String url = ACHIEVE_DAILY_URL;
        String jsonContentRequest = convertToString(resource("acceptance/request/addDailyReport.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseUserPointSumOnePoint.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddThanks() throws IOException {

        //given
        String url = ACHIEVE_THANKS_URL;
        String jsonContentRequest = convertToString(resource("acceptance/request/addFirstThanks.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumOnePoint.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddTwoThanks() throws IOException {

        //given
        String url = ACHIEVE_THANKS_URL;
        String jsonFirstContentRequest = convertToString(resource("acceptance/request/addFirstThanks.json"));
        String jsonSecondContentRequest = convertToString(resource("acceptance/request/addSecondThanks.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumTwoThanks.json"));

        //when
        Response actualResponse = getResponse(url, jsonFirstContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
        actualResponse = getResponse(url, jsonSecondContentRequest, HttpMethod.POST);
        printConsoleReport(url, TWO_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddCodenjoy() throws IOException {

        //given
        String url = ACHIEVE_CODENJOY_URL;
        String jsonContentRequest = convertToString(resource("acceptance/request/addCodenjoy.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumCodenjoy.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, THREE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddInterview() throws IOException {

        //given
        String url = ACHIEVE_INTERVIEW_URL;
        String jsonContentRequest = convertToString(resource("acceptance/request/addInterview.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumTenPoints.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddWelcome() throws IOException {

        //given
        String url = ACHIEVE_WELCOME_URL;
        String jsonContentRequest = convertToString(resource("acceptance/request/addWelcome.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumOnePoint.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddTeam() throws IOException {

        //given
        String url = ACHIEVE_TEAM_URL;
        String jsonContentRequest = convertToString(resource("acceptance/request/addTeam.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumTeam.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, FOUR_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        //then
        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    Response getControlResponse() {
        return getResponse(USER_POINT_SUM_URL, EMPTY_JSON_CONTENT_REQUEST, HttpMethod.GET);
    }
}
