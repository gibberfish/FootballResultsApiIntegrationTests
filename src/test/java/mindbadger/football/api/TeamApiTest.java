package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.withTeam;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class TeamApiTest extends AbstractRestAssuredTest {

	@Test
	public void shouldReturnNotFoundWhenGettingNonExistentTeam () {
		whenGet(TEAM_URL, NON_EXISTENT_TEAM_ID).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldCreateANewTeam () {
		String newTeamId = whenCreate(TEAM_URL, withTeam(TEAM1_NAME)).
				then().
				statusCode(HttpStatus.SC_CREATED).
				contentType(ContentType.JSON).extract().path("data.id");

		newTeamIds.add(newTeamId);

		whenGet(TEAM_URL, newTeamId).
			then().
				statusCode(HttpStatus.SC_OK). //TODO This should be SC_CREATED 201
			assertThat().
				body("data.attributes.teamName", equalTo(TEAM1_NAME));
	}

	@Test
	public void shouldDeleteAnExistingTeam () {
		String newTeamId = whenCreate(TEAM_URL, withTeam(TEAM1_NAME)).
			then().
				statusCode(HttpStatus.SC_CREATED).
				contentType(ContentType.JSON).extract().path("data.id");

		newTeamIds.add(newTeamId);

		whenDelete(TEAM_URL, newTeamId).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);

		whenGet(TEAM_URL, newTeamId).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldThrowAnErrorWhenAttemptToCreateATeamWithADuplicateName () {
		String newTeamId = whenCreate(TEAM_URL, withTeam(TEAM1_NAME)).
				then().
				statusCode(HttpStatus.SC_CREATED).
				contentType(ContentType.JSON).extract().path("data.id");

		newTeamIds.add(newTeamId);

		whenCreate(TEAM_URL, withTeam(TEAM1_NAME)).
				then().
				statusCode(HttpStatus.SC_BAD_REQUEST);
	}
}
