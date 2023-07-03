import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SecurityTests {
    private static Response response;

    @ParameterizedTest
    @DisplayName("Verify that unauthorized users restricted to get the response")
    @ValueSource(strings = {"helloWorld", "anInvalidApiKey1234455"})
    public void invalidApiKeyTest(String apiKey) {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=" + apiKey);
        System.out.println(response.asString());
        response.then().statusCode(401);
        response.then().body("message", equalTo(Consts.INVALID_API_KEY_ERROR));
    }

    @Test
    @DisplayName("Verify that users requested without access key restricted to get the response")
    public void missingApiKeyTest() {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT);
        System.out.println(response.asString());
        response.then().statusCode(401);
        response.then().body("message", equalTo(Consts.MISSING_API_KEY_ERROR));
    }
}
