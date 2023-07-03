import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class HistoricalEndpointNegativeTests {
    private static Response response;

    @Test
    @DisplayName("Verify that an error returned when requested without date parameter")
    public void missingDateParamTest() {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.MISSING_DATE_ERROR));
        response.then().body("error.code", equalTo(301));
    }

    @ParameterizedTest
    @DisplayName("Verify that an error returned when requested with invalid date parameter")
    @ValueSource(strings = {"2018/08/08", "22.12.2020"})
    public void wrongDateParamTest(String date) {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=" + date);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.INVALID_DATE_ERROR));
        response.then().body("error.code", equalTo(302));
    }

    @ParameterizedTest
    @DisplayName("Verify that an error returned when requested with invalid source parameter")
    @ValueSource(strings = {"DOLLAR", "C24"})
    public void wrongSourceParamTest(String source) {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=2023-01-01" + "&source=" + source);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.INVALID_SOURCE_ERROR));
        response.then().body("error.code", equalTo(201));
    }

    @ParameterizedTest
    @DisplayName("Verify that an error returned when requested with invalid currency parameter")
    @ValueSource(strings = {"EURO", "123"})
    public void wrongCurrencyParamTest(String currency) {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=2000-09-11" + "&currencies=" + currency);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.INVALID_CURRENCY_ERROR));
        response.then().body("error.code", equalTo(202));
    }

    @ParameterizedTest
    @DisplayName("Verify that an error returned when requested with more than one invalid currency parameters")
    @CsvSource({"dinar, 200", "BUG, %8+"})
    public void invalidCurrencyParamsTest(String currency1, String currency2) {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=2020-09-11" + "&currencies=" + currency1 + "," + currency2);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.INVALID_CURRENCY_ERROR));
        response.then().body("error.code", equalTo(202));
    }

    @ParameterizedTest
    @DisplayName("Verify that an error returns when one of the requested currency parameter is invalid")
    @CsvSource({"123, ILS", "RUB, %a7"})
    public void invalidAndValidCurrencyParamsTest(String currency1, String currency2) {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=2007-08-12" + "&currencies=" + currency1 + "," + currency2);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.info", containsString(Consts.INVALID_CURRENCY_ERROR));
        response.then().body("error.code", equalTo(202));
    }
}
