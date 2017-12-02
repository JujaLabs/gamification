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

    private static final String EMPTY_JSON_CONTENT_REQUEST = "";
    private static final String ONE_ACHIEVEMENT_ID = "achievement id";
    private static final String TWO_ACHIEVEMENT_ID = "[achievement1 id, achievement2 id]";
    private static final String THREE_ACHIEVEMENT_ID = "[ achievement1 id, achievement2 id, achievement3 id]";
    private static final String FOUR_ACHIEVEMENT_ID = "[ achievement1 id, achievement2 id, achievement3 id, " +
            "achievement4 id]";

    private static final String ACHIEVEMENTS_ADD_DAILY_URL = "/v1/gamification/achievements/daily";
    private static final String ACHIEVEMENTS_ADD_THANKS_URL = "/v1/gamification/achievements/thanks";
    private static final String ACHIEVEMENTS_ADD_CODENJOY_URL = "/v1/gamification/achievements/codenjoy";
    private static final String ACHIEVEMENTS_ADD_INTERVIEW_URL = "/v1/gamification/achievements/interview";
    private static final String ACHIEVEMENTS_ADD_KEEPER_THANKS_URL = "/v1/gamification/achievements/keepers/thanks";
    private static final String ACHIEVEMENTS_ADD_WELCOME_URL = "/v1/gamification/achievements/welcome";
    private static final String ACHIEVEMENTS_ADD_TEAM_URL = "/v1/gamification/achievements/team";
    private static final String USERS_GET_POINT_SUM = "/v1/gamification/users/pointSum";

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddDailyReport() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addDailyReport.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseUserPointSumOnePoint.json"));

        Response actualResponse = getResponse(ACHIEVEMENTS_ADD_DAILY_URL, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(ACHIEVEMENTS_ADD_DAILY_URL, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddThanks() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addFirstThanks.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumOnePoint.json"));

        Response actualResponse = getResponse(ACHIEVEMENTS_ADD_THANKS_URL, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(ACHIEVEMENTS_ADD_THANKS_URL, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddTwoThanks() throws IOException {
        String jsonFirstContentRequest = convertToString(resource("acceptance/request/addFirstThanks.json"));
        String jsonSecondContentRequest = convertToString(resource("acceptance/request/addSecondThanks.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumTwoThanks.json"));

        Response actualResponse = getResponse(ACHIEVEMENTS_ADD_THANKS_URL, jsonFirstContentRequest, HttpMethod.POST);
        printConsoleReport(ACHIEVEMENTS_ADD_THANKS_URL, ONE_ACHIEVEMENT_ID, actualResponse.body());
        actualResponse = getResponse(ACHIEVEMENTS_ADD_THANKS_URL, jsonSecondContentRequest, HttpMethod.POST);
        printConsoleReport(ACHIEVEMENTS_ADD_THANKS_URL, TWO_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddCodenjoy() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addCodenjoy.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumCodenjoy.json"));

        Response actualResponse = getResponse(ACHIEVEMENTS_ADD_CODENJOY_URL, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(ACHIEVEMENTS_ADD_CODENJOY_URL, THREE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddInterview() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addInterview.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumTenPoints.json"));

        Response actualResponse = getResponse(ACHIEVEMENTS_ADD_INTERVIEW_URL, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(ACHIEVEMENTS_ADD_INTERVIEW_URL, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddWelcome() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addWelcome.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumOnePoint.json"));

        Response actualResponse = getResponse(ACHIEVEMENTS_ADD_WELCOME_URL, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(ACHIEVEMENTS_ADD_WELCOME_URL, ONE_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddTeam() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addTeam.json"));
        String jsonContentControlResponse = convertToString(resource(
                "acceptance/response/responseUserPointSumTeam.json"));

        Response actualResponse = getResponse(ACHIEVEMENTS_ADD_TEAM_URL, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(ACHIEVEMENTS_ADD_TEAM_URL, FOUR_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    Response getControlResponse() {
        return getResponse(USERS_GET_POINT_SUM, EMPTY_JSON_CONTENT_REQUEST, HttpMethod.GET);
    }
}
