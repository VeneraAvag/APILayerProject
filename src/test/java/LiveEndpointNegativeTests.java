import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.hamcrest.Matchers.*;

public class LiveEndpointNegativeTests {
    private static Response response;

    @ParameterizedTest
    @DisplayName("Verify that an error returns when request with invalid currency parameter")
    @ValueSource(strings = {"peso", "true"})
    public void invalidCurrencyTest(String currency) {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + Consts.API_KEY + "&currencies=" + currency);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.INVALID_CURRENCY_ERROR));
        response.then().body("error.code", equalTo(202));
    }

    @ParameterizedTest
    @DisplayName("Verify that an error returned when one of the requested currency parameter is invalid")
    @CsvSource({"3BB, EUR", "RUB, $20"})
    public void validAndInvalidCurrencyParamsTest(String currency1, String currency2) {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + Consts.API_KEY + "&currencies=" + currency1 + "," + currency2);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.INVALID_CURRENCY_ERROR));
        response.then().body("error.code", equalTo(202));
    }

    @ParameterizedTest
    @DisplayName("Verify that an error returns when request with invalid source currency")
    @ValueSource(strings = {"ruble", "$CAD"})
    public void invalidSourceCurrencyTest(String source) {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + Consts.API_KEY + "&source=" + source + "&currencies=" + Consts.CAD);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.INVALID_SOURCE_ERROR));
        response.then().body("error.code", equalTo(201));
    }
}
