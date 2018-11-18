package mindbadger.football.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static com.jayway.restassured.RestAssured.given;
import static mindbadger.football.api.helpers.MessageCreationHelper.withDivision;
import static mindbadger.football.api.helpers.MessageCreationHelper.withSeason;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static mindbadger.football.api.ApiTestConstants.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class DivisionApiTest {

	private String newDivisionId;

	@Before
	public void setup() throws IOException {
		Properties prop = new Properties();
		prop.load(DivisionApiTest.class.getClassLoader().getResourceAsStream("application.properties"));

		String port = prop.getProperty("server.port");
		RestAssured.port = Integer.valueOf(port);

		String basePath = prop.getProperty("server.base");
		RestAssured.basePath = basePath;

		String baseHost = prop.getProperty("server.host");
		RestAssured.baseURI = baseHost;
	}

	@After
	public void deleteTestDataAtTheEnd() {
		given().delete(DIVISION_URL + newDivisionId);
	}

	// ****************************************************************************
	
	@Test
	public void createDivisionCheckAndThenDelete () {

		newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
			then().
				statusCode(HttpStatus.SC_CREATED).
				contentType(ContentType.JSON).extract().path("data.id");

		whenGet(DIVISION_URL, newDivisionId).
			then().
				statusCode(HttpStatus.SC_OK).
			assertThat().
				body("data.attributes.divisionName", equalTo(DIVISION1_NAME));

		whenDelete(DIVISION_URL, newDivisionId).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);
	}
}
