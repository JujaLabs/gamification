package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Option;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("/v1/gamification/achievements/daily")
    private String achievementsAddDailyUrl;
    @Value("/v1/gamification/achievements/thanks")
    private String achievementsAddThanksUrl;
    @Value("/v1/gamification/achievements/codenjoy")
    private String achievementsAddCodenjoyUrl;
    @Value("/v1/gamification/achievements/interview")
    private String achievementsAddInterviewUrl;
    @Value("/v1/gamification/achievements/keepers/thanks")
    private String achievementsAddKeeperThanksUrl;
    @Value("/v1/gamification/achievements/welcome")
    private String achievementsAddWelcomeUrl;
    @Value("/v1/gamification/achievements/team")
    private String achievementsAddTeamUrl;
    @Value("/v1/gamification/users/pointSum")
    private String usersGetPointSum;

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testAddDailyReport() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addDailyReport.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseUserPointSumOnePoint.json"));

        Response actualResponse = getResponse(achievementsAddDailyUrl, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(achievementsAddDailyUrl, ONE_ACHIEVEMENT_ID, actualResponse.body());
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

        Response actualResponse = getResponse(achievementsAddThanksUrl, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(achievementsAddThanksUrl, ONE_ACHIEVEMENT_ID, actualResponse.body());
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

        Response actualResponse = getResponse(achievementsAddThanksUrl, jsonFirstContentRequest, HttpMethod.POST);
        printConsoleReport(achievementsAddThanksUrl, ONE_ACHIEVEMENT_ID, actualResponse.body());
        actualResponse = getResponse(achievementsAddThanksUrl, jsonSecondContentRequest, HttpMethod.POST);
        printConsoleReport(achievementsAddThanksUrl, TWO_ACHIEVEMENT_ID, actualResponse.body());
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

        Response actualResponse = getResponse(achievementsAddCodenjoyUrl, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(achievementsAddCodenjoyUrl, THREE_ACHIEVEMENT_ID, actualResponse.body());
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

        Response actualResponse = getResponse(achievementsAddInterviewUrl, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(achievementsAddInterviewUrl, ONE_ACHIEVEMENT_ID, actualResponse.body());
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

        Response actualResponse = getResponse(achievementsAddWelcomeUrl, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(achievementsAddWelcomeUrl, ONE_ACHIEVEMENT_ID, actualResponse.body());
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

        Response actualResponse = getResponse(achievementsAddTeamUrl, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(achievementsAddTeamUrl, FOUR_ACHIEVEMENT_ID, actualResponse.body());
        Response controlResponse = getControlResponse();

        assertThatJson(controlResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    Response getControlResponse() {
        return getResponse(usersGetPointSum, EMPTY_JSON_CONTENT_REQUEST, HttpMethod.GET);
    }
}
