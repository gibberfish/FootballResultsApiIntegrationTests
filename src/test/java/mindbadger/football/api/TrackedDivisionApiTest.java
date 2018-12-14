package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.withTrackedDivision;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static mindbadger.football.api.helpers.TestPreConditionHelper.givenATrackedDivisionWith;
import static org.hamcrest.Matchers.equalTo;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class TrackedDivisionApiTest extends AbstractRestAssuredTest {

	@Test
	public void shouldReturnNotFoundWhenGettingNonExistentTrackedDivision () {
		whenGet(TRACKED_DIVISION_URL, NON_EXISTENT_TRACKED_DIVISION_ID).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldCreateANewTrackedDivision () {
		String newTrackedDivisionId = whenCreate(TRACKED_DIVISION_URL, withTrackedDivision(DIALECT, SOURCE_ID)).
				then().
				statusCode(HttpStatus.SC_CREATED).
				contentType(ContentType.JSON).extract().path("data.id");

		newTrackedDivisionIds.add(newTrackedDivisionId);

		whenGet(TRACKED_DIVISION_URL, newTrackedDivisionId).
			then().
				statusCode(HttpStatus.SC_OK).
			assertThat().
				body("data.attributes.dialect", equalTo(DIALECT)).
				body("data.attributes.sourceId", equalTo(Integer.parseInt(SOURCE_ID)));
	}

	@Test
	public void shouldDeleteAnExistingTrackedDivision () {
		String newTrackedDivisionId = givenATrackedDivisionWith(DIALECT, SOURCE_ID);

		newTrackedDivisionIds.add(newTrackedDivisionId);

		whenDelete(TRACKED_DIVISION_URL, newTrackedDivisionId).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);

		whenGet(TRACKED_DIVISION_URL, newTrackedDivisionId).
			then().
				statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void shouldThrowAnErrorWhenAttemptToCreateATrackedDivisionWithADuplicateName () {
		String newTrackedDivisionId = givenATrackedDivisionWith(DIALECT, SOURCE_ID);

		newTrackedDivisionIds.add(newTrackedDivisionId);

		whenCreate(TRACKED_DIVISION_URL, withTrackedDivision(DIALECT, SOURCE_ID)).
				then().
				statusCode(HttpStatus.SC_BAD_REQUEST);
	}
}
