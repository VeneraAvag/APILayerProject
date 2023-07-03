import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiveEndpointPositiveTests {
    private static Response response;

    @Test
    @DisplayName("Verify response when request with default parameters")
    public void defaultParamsFunctionalTest() {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + Consts.API_KEY);
        System.out.println(response.asString());

        String expectedDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("source", equalTo(Consts.USD));
        response.then().body("quotes", hasKey("USDCAD"));
        response.then().body("quotes", hasKey("USDEUR"));
        response.then().body("quotes", hasKey("USDILS"));
        response.then().body("quotes", hasKey("USDRUB"));
        response.then().body("quotes.USDCAD", notNullValue());
        response.then().body("quotes.USDEUR", notNullValue());
        response.then().body("quotes.USDILS", notNullValue());
        response.then().body("quotes.USDRUB", notNullValue());
        assertEquals(expectedDate, Timestamp.timestampConverter(response));
    }

    @Test
    @DisplayName("Verify if response contain terms and privacy")
    public void termsAndPrivacyTest() {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + Consts.API_KEY);
        System.out.println(response.asString());
        response.then().body("terms", notNullValue());
        response.then().body("privacy", notNullValue());
    }

    @Test
    @DisplayName("Verify response when request with source and currency parameters")
    public void optionalParamsFunctionalTest() {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + Consts.API_KEY + "&source=" + Consts.CAD + "&currencies=" + Consts.USD);
        System.out.println(response.asString());

        String expectedDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("source", equalTo(Consts.CAD));
        response.then().body("quotes", hasKey("CADUSD"));
        response.then().body("quotes.CADUSD", notNullValue());
        assertEquals(expectedDate, Timestamp.timestampConverter(response));
    }

    @Test
    @DisplayName("Verify that response returns when requested with more than one currency parameters")
    public void severalCurrenciesRequestTest() {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + Consts.API_KEY + "&source=" + Consts.EUR + "&currencies=" + Consts.USD + "," + Consts.RUB + "," + Consts.CAD + "," + Consts.ILS);
        System.out.println(response.asString());

        String expectedDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("source", equalTo(Consts.EUR));
        response.then().body("quotes", hasKey("EURUSD"));
        response.then().body("quotes", hasKey("EURRUB"));
        response.then().body("quotes", hasKey("EURCAD"));
        response.then().body("quotes", hasKey("EURILS"));
        response.then().body("quotes.EURUSD", notNullValue());
        response.then().body("quotes.EURRUB", notNullValue());
        response.then().body("quotes.EURCAD", notNullValue());
        response.then().body("quotes.EURILS", notNullValue());
        assertEquals(expectedDate, Timestamp.timestampConverter(response));
    }
}
