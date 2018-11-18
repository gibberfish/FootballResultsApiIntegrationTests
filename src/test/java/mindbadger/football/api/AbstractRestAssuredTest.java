package mindbadger.football.api;

import com.jayway.restassured.RestAssured;
import org.junit.Before;

import java.io.IOException;
import java.util.Properties;

public class AbstractRestAssuredTest {
    protected String port;
    protected String basePath;
    protected String host;

    @Before
    public void setup() throws IOException {
        Properties prop = new Properties();
        prop.load(SeasonApiTest.class.getClassLoader().getResourceAsStream("application.properties"));

        port = prop.getProperty("server.port");
        RestAssured.port = Integer.valueOf(port);

        basePath = prop.getProperty("server.base");
        RestAssured.basePath = basePath;

        host = prop.getProperty("server.host");
        RestAssured.baseURI = host;
    }
}
