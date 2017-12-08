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

/**
 * @author Danil Kuznetsov
 */
@RunWith(SpringRunner.class)
public class UserAcceptanceTest extends BaseAcceptanceTest {

    private static final String EMPTY_JSON_CONTENT_REQUEST = "";
    private static final String USERS_GET_POINT_SUM = "/v1/gamification/users/pointSum";
    private static final String USERS_GET_ACHIEVEMENT_DETAILS = "/v1/gamification/users/achievementDetails";

    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testUrlPointSum() throws IOException {
        String expectedResponse = convertToString(resource("acceptance/response/responseUserPointSum.json"));

        Response actualResponse = getResponse(USERS_GET_POINT_SUM, EMPTY_JSON_CONTENT_REQUEST, HttpMethod.GET);
        printConsoleReport(USERS_GET_POINT_SUM, expectedResponse, actualResponse.body());

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }

    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testUrlAchieveDetails() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/selectAchieveDetails.json"));
        String expectedResponse = convertToString(resource("acceptance/response/responseUserAchieveDetails.json"));

        Response actualResponse = getResponse(USERS_GET_ACHIEVEMENT_DETAILS, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(USERS_GET_ACHIEVEMENT_DETAILS, expectedResponse, actualResponse.body());

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }
}
