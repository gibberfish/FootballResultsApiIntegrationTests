package mindbadger.football.api;

import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
//import org.springframework.beans.factory.annotation.Value;

//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestApplication.class})
public class ApiTest {
	private static final Integer SEASON_NUMBER = new Integer(1700);
	private static final String DIVISION1_NAME = "Madeup Division 1";
	private static final String DIVISION2_NAME = "Madeup Division 2";
	private static final String TEAM1_NAME = "Madeup Team 1";
	private static final String TEAM2_NAME = "Madeup Team 2";
	private static final String TEAM3_NAME = "Madeup Team 3";

//	@Value("${api.url}")
	private String apiUrl;


	@Before
	public void setup() throws IOException {
		Properties prop = new Properties();
		prop.load(ApiTest.class.getClassLoader().getResourceAsStream("application.properties"));


		String port = prop.getProperty("server.port");
		if (port == null) {
			//RestAssured.port = Integer.valueOf(1972);
		}
		else{
			RestAssured.port = Integer.valueOf(port);
		}

		String basePath = prop.getProperty("server.base");
		if(basePath==null){
			basePath = "/api/";
		}
		RestAssured.basePath = basePath;

		String baseHost = prop.getProperty("server.host");
		if(baseHost==null){
			baseHost = "http://localhost";
		}
		RestAssured.baseURI = baseHost;
	}

	@After
	public void tearDown() {

	}

	@Test
	public void test () {
		// Given

		// When
		given().
				when().
				get("/seasons/2017").
				then().
				statusCode(200).
				assertThat().
				body("data.id", equalTo("2017"));

		// Then

	}

}
