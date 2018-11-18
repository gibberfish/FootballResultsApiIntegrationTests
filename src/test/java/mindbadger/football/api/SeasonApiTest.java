package mindbadger.football.api;

import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.*;

import java.io.IOException;
import java.util.Properties;

import static com.jayway.restassured.RestAssured.given;
import static mindbadger.football.api.helpers.MessageCreationHelper.*;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static mindbadger.football.api.ApiTestConstants.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class SeasonApiTest {

	@Before
	public void setup() throws IOException {
		Properties prop = new Properties();
		prop.load(SeasonApiTest.class.getClassLoader().getResourceAsStream("application.properties"));

		String port = prop.getProperty("server.port");
		RestAssured.port = Integer.valueOf(port);

		String basePath = prop.getProperty("server.base");
		RestAssured.basePath = basePath;

		String baseHost = prop.getProperty("server.host");
		RestAssured.baseURI = baseHost;
	}

	@After
	public void deleteTestDataAtTheEnd() {
		given().delete(SEASON_URL + SEASON_NUMBER);
	}

	// ****************************************************************************

	@Test
	public void shouldReturnNotFoundWhenGettingNonExistentSeason () {
		whenGet(SEASON_URL, SEASON_NUMBER).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldCreateANewSeason () {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER)).
			then().
				statusCode(HttpStatus.SC_CREATED);

		whenGet(SEASON_URL, SEASON_NUMBER).
			then().
				statusCode(HttpStatus.SC_OK).
			assertThat().
				body("data.id", equalTo(SEASON_NUMBER));
	}

	@Test
	public void shouldDeleteAnExistingSeason () {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER)).
			then().
				statusCode(HttpStatus.SC_CREATED);

		whenDelete(SEASON_URL, SEASON_NUMBER).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);

		whenGet(SEASON_URL, SEASON_NUMBER).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldThrowAnErrorWhenAttemptToCreateADuplicateSeason () {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER)).
				then().
				statusCode(HttpStatus.SC_CREATED);

		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER)).
				then().
				statusCode(HttpStatus.SC_BAD_REQUEST);
	}
}
