import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;

public class HistoricalEndpointPositiveTests {
    private static Response response;

    @ParameterizedTest
    @DisplayName("Verify response when request with default parameters")
    @ValueSource(strings = {"2019-08-08", "2010-12-11"})
    public void defaultParamsFunctionalTest(String date) {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=" + date);
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("historical", equalTo(true));
        response.then().body("date", equalTo(date));
        response.then().body("source", equalTo(Consts.USD));
        response.then().body("quotes", hasKey("USDCAD"));
        response.then().body("quotes", hasKey("USDEUR"));
        response.then().body("quotes", hasKey("USDILS"));
        response.then().body("quotes", hasKey("USDRUB"));
        response.then().body("quotes.USDCAD", notNullValue());
        response.then().body("quotes.USDEUR", notNullValue());
        response.then().body("quotes.USDILS", notNullValue());
        response.then().body("quotes.USDRUB", notNullValue());
        assertEquals(date, Timestamp.timestampConverter(response));
    }

    @Test
    @DisplayName("Verify response when request with source and currency parameters")
    public void optionalParamsFunctionalTest() {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=2007-11-12" + "&source=CAD" + "&currencies=USD");
        System.out.println(response.asString());

        Float expectedRate = 1.041106f;

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("historical", equalTo(true));
        response.then().body("date", equalTo("2007-11-12"));
        response.then().body("source", equalTo("CAD"));
        response.then().body("quotes", hasKey("CADUSD"));
        response.then().body("quotes.CADUSD", equalTo(expectedRate));
        assertEquals("2007-11-12", Timestamp.timestampConverter(response));
    }

    @Test
    @DisplayName("Verify that response returns when requested more than one currency parameters")
    public void severalCurrenciesRequestTest() {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=2019-08-08" + "&source=CAD" + "&currencies=USD,RUB,EUR,ILS");
        System.out.println(response.asString());

        HashMap<String, Float> quotes = new HashMap<>();
        quotes.put("CADUSD", 0.755829f);
        quotes.put("CADRUB", 49.213702f);
        quotes.put("CADEUR", 0.675643f);
        quotes.put("CADILS", 2.627189f);

        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("historical", equalTo(true));
        response.then().body("date", equalTo("2019-08-08"));
        response.then().body("source", equalTo("CAD"));
        response.then().body("quotes", equalTo(quotes));
        assertEquals("2019-08-08", Timestamp.timestampConverter(response));
    }
}
