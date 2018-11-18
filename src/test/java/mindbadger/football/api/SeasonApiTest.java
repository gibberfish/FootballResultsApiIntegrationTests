package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.SEASON_NUMBER;
import static mindbadger.football.api.ApiTestConstants.SEASON_URL;
import static mindbadger.football.api.helpers.MessageCreationHelper.withSeason;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class SeasonApiTest extends AbstractRestAssuredTest {

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
				statusCode(HttpStatus.SC_OK). //TODO This should be SC_CREATED 201
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

	@Test
	public void shouldHaveAHyperlinkToSeasonDivisions () {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER)).
				then().
				statusCode(HttpStatus.SC_CREATED);

		String seasonDivisionUrl = whenGet(SEASON_URL, SEASON_NUMBER).
			then().
				contentType(ContentType.JSON).extract().path("data.relationships.seasonDivisions.links.related");

		String expectedUrl = host + ":" + port + basePath + SEASON_URL + SEASON_NUMBER + "/seasonDivisions";
		assertEquals (expectedUrl, seasonDivisionUrl);
	}
}
