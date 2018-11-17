package mindbadger.football.api;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.*;

import java.io.IOException;
import java.util.Properties;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {

	private static final String SEASON_URL = "seasons/";
	private static final String DIVISION_URL = "divisions/";
	private static final String TEAM_URL = "team/";

	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String CRNK_CONTENT_TYPE = "application/vnd.api+json";

	private static final String SEASON_NUMBER = "1750";
	private static final String DIVISION1_NAME = "Madeup Division 1";
	private static final String DIVISION2_NAME = "Madeup Division 2";
	private static final String TEAM1_NAME = "Madeup Team 1";
	private static final String TEAM2_NAME = "Madeup Team 2";
	private static final String TEAM3_NAME = "Madeup Team 3";

	@Before
	public void setup() throws IOException {
		Properties prop = new Properties();
		prop.load(ApiTest.class.getClassLoader().getResourceAsStream("application.properties"));

		String port = prop.getProperty("server.port");
		RestAssured.port = Integer.valueOf(port);

		String basePath = prop.getProperty("server.base");
		RestAssured.basePath = basePath;

		String baseHost = prop.getProperty("server.host");
		RestAssured.baseURI = baseHost;
	}

	@After
	public void tearDown() {
		//given().delete(SEASON_URL + SEASON_NUMBER);
	}

	@Test
	public void createSeasonCheckAndThenDelete () {
		whenCreateSeason(SEASON_NUMBER).
			then().
				statusCode(HttpStatus.SC_CREATED);

		whenGet(SEASON_URL, SEASON_NUMBER).
			then().
				statusCode(HttpStatus.SC_OK).
			assertThat().
				body("data.id", equalTo(SEASON_NUMBER));

		whenDelete(SEASON_URL, SEASON_NUMBER).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);
	}

	@Test
	public void createDivisionCheckAndThenDelete () {
		Response response = whenCreateDivision(DIVISION1_NAME);

		response.
			then().
				statusCode(HttpStatus.SC_CREATED);

		String divisionId = response.then().contentType(ContentType.JSON).extract().path("data.id");

		whenGet(DIVISION_URL, divisionId).
				then().
				statusCode(HttpStatus.SC_OK).
				assertThat().
				body("data.attributes.divisionName", equalTo(DIVISION1_NAME));

		whenDelete(DIVISION_URL, divisionId).
				then().
				statusCode(HttpStatus.SC_NO_CONTENT);
	}

	// ****************************************************************************

	private Response whenDelete(String url, String season) {
		return
			given().
				header(CONTENT_TYPE_HEADER, CRNK_CONTENT_TYPE).
			when().
				delete(url + season);
	}

	private Response whenGet(String url, String season) {
		return
			given().
			when().
				get(url + season);
	}

	private Response whenCreateSeason(String season) {
		JsonObject post = new JsonObject();
		JsonObject data = new JsonObject();
		JsonObject attributes = new JsonObject();
		post.add("data", data);
		data.addProperty("id", season);
		data.addProperty("type", "seasons");
		data.add("attributes", attributes);
		attributes.addProperty("seasonNumber", season);

		return
			with().
				body(post).
			given().
				header(CONTENT_TYPE_HEADER, CRNK_CONTENT_TYPE).
			when().
				post(SEASON_URL);
	}

	private Response whenCreateDivision(String divisionName) {
		JsonObject post = new JsonObject();
		JsonObject data = new JsonObject();
		JsonObject attributes = new JsonObject();
		post.add("data", data);
		data.addProperty("type", "divisions");
		data.add("attributes", attributes);
		attributes.addProperty("divisionName", divisionName);

		return
			with().
				body(post).
			given().
				header(CONTENT_TYPE_HEADER, CRNK_CONTENT_TYPE).
			when().
				post(DIVISION_URL);
	}

}
