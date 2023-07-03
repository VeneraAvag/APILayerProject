import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class PerformanceTests {
    private static Response response;

    @Test
    public void liveEndpointPerformanceTest() {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + Consts.API_KEY + "&source=EUR" + "&currencies=USD,CAD,RUB,ILS");

        response.then().time(lessThan(3000L));
    }

    @Test
    public void historicalEndpointPerformanceTest() {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "?apikey=" + Consts.API_KEY + "&date=2019-08-08" + "&source=CAD" + "&currencies=USD,RUB,EUR,ILS");

        response.then().time(lessThan(3000L));
    }
}
