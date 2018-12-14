package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.withTeamMapping;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static mindbadger.football.api.helpers.TestPreConditionHelper.givenATeamMappingWith;
import static org.hamcrest.Matchers.equalTo;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class TeamMappingApiTest extends AbstractRestAssuredTest {

	@Test
	public void shouldReturnNotFoundWhenGettingNonExistentTeamMapping () {
		whenGet(TEAM_MAPPING_URL, NON_EXISTENT_TEAM_MAPPING_ID).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldCreateANewTeamMapping () {
		String newTeamMappingId = whenCreate(TEAM_MAPPING_URL, withTeamMapping(DIALECT, SOURCE_ID, FRA_ID)).
				then().
				statusCode(HttpStatus.SC_CREATED).
				contentType(ContentType.JSON).extract().path("data.id");

		newTeamMappingIds.add(newTeamMappingId);

		whenGet(TEAM_MAPPING_URL, newTeamMappingId).
			then().
				statusCode(HttpStatus.SC_OK).
			assertThat().
				body("data.attributes.dialect", equalTo(DIALECT)).
				body("data.attributes.sourceId", equalTo(Integer.parseInt(SOURCE_ID))).
				body("data.attributes.fraId", equalTo(Integer.parseInt(FRA_ID)));
	}

	@Test
	public void shouldDeleteAnExistingTeam () {
		String newTeamMappingId = givenATeamMappingWith(DIALECT, SOURCE_ID, FRA_ID);

		newTeamMappingIds.add(newTeamMappingId);

		whenDelete(TEAM_MAPPING_URL, newTeamMappingId).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);

		whenGet(TEAM_MAPPING_URL, newTeamMappingId).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldThrowAnErrorWhenAttemptToCreateATeamWithADuplicateName () {
		String newTeamMappingId = givenATeamMappingWith(DIALECT, SOURCE_ID, FRA_ID);

		newTeamMappingIds.add(newTeamMappingId);

		whenCreate(TEAM_MAPPING_URL, withTeamMapping(DIALECT, SOURCE_ID, FRA_ID)).
				then().
				statusCode(HttpStatus.SC_BAD_REQUEST);
	}
}
