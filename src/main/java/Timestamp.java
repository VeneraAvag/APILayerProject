import io.restassured.response.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Timestamp {
    public static String timestampConverter(Response response) {
        Integer timestamp = response.path("timestamp");
        long epochSeconds = (long) timestamp;

        LocalDateTime ldt = Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        String actualDate = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return actualDate;
    }
}
