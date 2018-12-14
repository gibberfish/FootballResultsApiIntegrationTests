package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.withDivision;
import static mindbadger.football.api.helpers.MessageCreationHelper.withDivisionMapping;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static mindbadger.football.api.helpers.TestPreConditionHelper.givenADivisionMappingWith;
import static org.hamcrest.Matchers.equalTo;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class DivisionMappingApiTest extends AbstractRestAssuredTest {

	@Test
	public void shouldReturnNotFoundWhenGettingNonExistentDivisionMapping () {
		whenGet(DIVISION_MAPPING_URL, NON_EXISTENT_DIVISION_MAPPING_ID).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldCreateANewDivisionMapping () {
		String newDivisionMappingId = whenCreate(DIVISION_MAPPING_URL, withDivisionMapping(DIALECT, SOURCE_ID, FRA_ID)).
				then().
				statusCode(HttpStatus.SC_CREATED).
				contentType(ContentType.JSON).extract().path("data.id");

		newDivisionMappingIds.add(newDivisionMappingId);

		whenGet(DIVISION_MAPPING_URL, newDivisionMappingId).
			then().
				statusCode(HttpStatus.SC_OK).
			assertThat().
				body("data.attributes.dialect", equalTo(DIALECT)).
				body("data.attributes.sourceId", equalTo(Integer.parseInt(SOURCE_ID))).
				body("data.attributes.fraId", equalTo(Integer.parseInt(FRA_ID)));
	}

	@Test
	public void shouldDeleteAnExistingDivision () {
		String newDivisionMappingId = givenADivisionMappingWith(DIALECT, SOURCE_ID, FRA_ID);

		newDivisionMappingIds.add(newDivisionMappingId);

		whenDelete(DIVISION_MAPPING_URL, newDivisionMappingId).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);

		whenGet(DIVISION_MAPPING_URL, newDivisionMappingId).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldThrowAnErrorWhenAttemptToCreateADivisionWithADuplicateName () {
		String newDivisionMappingId = givenADivisionMappingWith(DIALECT, SOURCE_ID, FRA_ID);

		newDivisionMappingIds.add(newDivisionMappingId);

		whenCreate(DIVISION_MAPPING_URL, withDivisionMapping(DIALECT, SOURCE_ID, FRA_ID)).
				then().
				statusCode(HttpStatus.SC_BAD_REQUEST);
	}
}
