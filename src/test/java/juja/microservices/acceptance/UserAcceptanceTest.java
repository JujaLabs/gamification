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

    private static final String USER_POINT_SUM_URL = "/v1/user/pointSum";
    private static final String USER_ACHIEVE_DETAILS_URL = "/v1/user/achieveDetails";
    private static final String EMPTY_JSON_CONTENT_REQUEST = "";

    @UsingDataSet(locations = "/datasets/addNewUsersAndAchievement.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void testUrlPointSum() throws IOException {

        //given
        String url = USER_POINT_SUM_URL;
        String expectedResponse = convertToString(resource("acceptance/response/responseUserPointSum.json"));

        //when
        Response actualResponse = getResponse(url, EMPTY_JSON_CONTENT_REQUEST, HttpMethod.GET);
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
        String url = USER_ACHIEVE_DETAILS_URL;
        String jsonContentRequest = convertToString(resource("acceptance/request/selectAchieveDetails.json"));
        String expectedResponse = convertToString(resource("acceptance/response/responseUserAchieveDetails.json"));

        //when
        Response actualResponse = getResponse(url, jsonContentRequest, HttpMethod.POST);
        printConsoleReport(url, expectedResponse, actualResponse.body());

        //then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }
}
