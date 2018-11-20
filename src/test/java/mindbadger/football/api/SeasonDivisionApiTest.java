package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.*;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static org.hamcrest.Matchers.*;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class SeasonDivisionApiTest extends AbstractRestAssuredTest {

	@Test
	public void seasonDivisionHyperlinkForNewSeasonShouldReturnNoSeasonDivisions() {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

		whenGet(SEASON_TO_SEASON_DIVISION_URL).
				then().
				statusCode(HttpStatus.SC_OK).
				assertThat().
				body("data", empty());
	}

	@Test
	public void shouldAddNewDivisionToSeason() {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

		String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
				then().
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionIds.add(newDivisionId);

		final String SEASON_DIVISION_ID = SEASON_NUMBER + "-" + newDivisionId;

		whenCreate(SEASON_TO_SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, newDivisionId, "1")).
				then().
				statusCode(HttpStatus.SC_CREATED).
				assertThat().
				body("data.id", equalTo(SEASON_DIVISION_ID));

		whenGet(SEASON_TO_SEASON_DIVISION_URL).
				then().
				statusCode(HttpStatus.SC_OK).
				assertThat().
				body("data.size()", is(1)).
				body("data[0].id", equalTo(SEASON_DIVISION_ID));
	}

	@Test
	public void shouldAddMultipleNewDivisionsToSeason() {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

		String newDivision1Id = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
				then().
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionIds.add(newDivision1Id);

		final String SEASON_DIVISION_1_ID = SEASON_NUMBER + "-" + newDivision1Id;

		whenCreate(SEASON_TO_SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, newDivision1Id, "1")).
				then().
				statusCode(HttpStatus.SC_CREATED).
				assertThat().
				body("data.id", equalTo(SEASON_DIVISION_1_ID));

		String newDivision2Id = whenCreate(DIVISION_URL, withDivision(DIVISION2_NAME)).
				then().
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionIds.add(newDivision2Id);

		final String SEASON_DIVISION_2_ID = SEASON_NUMBER + "-" + newDivision2Id;

		whenCreate(SEASON_TO_SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, newDivision2Id, "2")).
				then().
				statusCode(HttpStatus.SC_CREATED).
				assertThat().
				body("data.id", equalTo(SEASON_DIVISION_2_ID));


		whenGet(SEASON_TO_SEASON_DIVISION_URL).
				then().
				statusCode(HttpStatus.SC_OK).
				assertThat().
				body("data.size()", is(2));
	}

	@Test
	public void shouldReturnNotFoundWhenGettingNonExistentSeasonDivision() {
		whenGet(SEASON_TO_NON_EXISTENT_SEASON_DIVISION_URL).
				then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldAddNewCanonicalSeasonDivision() {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

		String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
				then().
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionIds.add(newDivisionId);

		final String SEASON_DIVISION_ID = SEASON_NUMBER + "-" + newDivisionId;

		whenCreate(SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, newDivisionId, "1")).
				then().
				statusCode(HttpStatus.SC_CREATED).
				assertThat().
				body("data.id", equalTo(SEASON_DIVISION_ID));

		whenGet(SEASON_DIVISION_URL, SEASON_DIVISION_ID).
				then().
				statusCode(HttpStatus.SC_OK).
				assertThat().
				body("data.id", equalTo(SEASON_DIVISION_ID));
	}

	@Test
	public void shouldDeleteASeasonDivision() {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

		String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
				then().
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionIds.add(newDivisionId);

		final String SEASON_DIVISION_ID = SEASON_NUMBER + "-" + newDivisionId;

		whenCreate(SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, newDivisionId, "1"));

		whenDelete(SEASON_DIVISION_URL, SEASON_DIVISION_ID).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);

		whenGet(SEASON_DIVISION_URL, SEASON_DIVISION_ID).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldThrowAnErrorWhenAttemptToCreateADuplicateSeasonDivision () {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

		String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
				then().
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionIds.add(newDivisionId);

		final String SEASON_DIVISION_ID = SEASON_NUMBER + "-" + newDivisionId;

		whenCreate(SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, newDivisionId, "1")).
				then().
				statusCode(HttpStatus.SC_CREATED);

		whenCreate(SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, newDivisionId, "1")).
				then().
				statusCode(HttpStatus.SC_CONFLICT);
	}

	@Test
	public void shouldThrowAnErrorWhenAttemptToCreateASeasonDivisionWithANonExistentDivision () {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

		whenCreate(SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, NON_EXISTENT_DIVISION_ID, "1")).
				then().
				statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void shouldThrowAnErrorWhenAttemptToCreateASeasonDivisionWithANonExistentSeason () {
		String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
				then().
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionIds.add(newDivisionId);

		whenCreate(SEASON_DIVISION_URL,
				withSeasonDivision(NON_EXISTENT_SEASON_NUM, newDivisionId, "1")).
				then().
				statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	public void shouldHaveAHyperlinksToSeasonDivisionTeamsAndFixtureDates () {
		whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

		String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
				then().
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionIds.add(newDivisionId);

		final String SEASON_DIVISION_ID = SEASON_NUMBER + "-" + newDivisionId;

		whenCreate(SEASON_DIVISION_URL,
				withSeasonDivision(SEASON_NUMBER, newDivisionId, "1")).
				then().
				statusCode(HttpStatus.SC_CREATED).
				assertThat().
				body("data.id", equalTo(SEASON_DIVISION_ID));


		String divisionHyperlink = host + ":" + port + basePath +
			SEASON_DIVISION_URL + SEASON_NUMBER + "-" + newDivisionId + "/division";

		String seasonHyperlink = host + ":" + port + basePath +
				SEASON_DIVISION_URL + SEASON_NUMBER + "-" + newDivisionId + "/season";

		String teamsHyperlink = host + ":" + port + basePath +
				SEASON_DIVISION_URL + SEASON_NUMBER + "-" + newDivisionId + "/teams";

		String fixtureDatesHyperlink = host + ":" + port + basePath +
				SEASON_DIVISION_URL + SEASON_NUMBER + "-" + newDivisionId + "/fixtureDates";

		whenGet(SEASON_DIVISION_URL + SEASON_NUMBER + "-" + newDivisionId).
			then().
			assertThat().
				body("data.relationships.division.links.related", equalTo(divisionHyperlink)).
				body("data.relationships.season.links.related", equalTo(seasonHyperlink)).
				body("data.relationships.teams.links.related", equalTo(teamsHyperlink)).
				body("data.relationships.fixtureDates.links.related", equalTo(fixtureDatesHyperlink))
		;

	}
}