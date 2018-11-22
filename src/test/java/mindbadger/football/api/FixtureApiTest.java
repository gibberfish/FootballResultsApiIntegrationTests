package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.*;
import static mindbadger.football.api.helpers.OperationHelper.whenCreate;
import static mindbadger.football.api.helpers.OperationHelper.whenGet;
import static mindbadger.football.api.helpers.TestPreConditionHelper.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class FixtureApiTest extends AbstractRestAssuredTest {

    @Test
    public void shouldCreateFixtureWithNoDate() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String fixtureId = whenCreate(FIXTURE_URL, withFixtureWithNoDate(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId)).
                then().
                statusCode(HttpStatus.SC_CREATED).
                assertThat().
                body("data.id", notNullValue()).
                contentType(ContentType.JSON).extract().path("data.id");
        newFixtureIds.add(fixtureId);

        whenGet(FIXTURE_URL, fixtureId).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(fixtureId)).
                body("data.attributes.fixtureDate", nullValue()).
                body("data.attributes.divisionId", equalTo(newDivisionId)).
                body("data.attributes.homeTeamId", equalTo(newHomeTeamId)).
                body("data.attributes.awayTeamId", equalTo(newAwayTeamId)).
                body("data.attributes.seasonNumber", equalTo(Integer.parseInt(SEASON_NUMBER))).
                body("data.attributes.homeGoals", nullValue()).
                body("data.attributes.awayGoals", nullValue());
    }

    @Test
    public void shouldCreateUnplayedFixtureWithDate() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String fixtureId = whenCreate(FIXTURE_URL, withFixtureWithDate(SEASON_NUMBER, newDivisionId,
                newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1)).
                then().
                statusCode(HttpStatus.SC_CREATED).
                assertThat().
                body("data.id", notNullValue()).
                contentType(ContentType.JSON).extract().path("data.id");
        newFixtureIds.add(fixtureId);

        whenGet(FIXTURE_URL, fixtureId).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(fixtureId)).
                body("data.attributes.fixtureDate", equalTo(FIXTURE_DATE_1)).
                body("data.attributes.divisionId", equalTo(newDivisionId)).
                body("data.attributes.homeTeamId", equalTo(newHomeTeamId)).
                body("data.attributes.awayTeamId", equalTo(newAwayTeamId)).
                body("data.attributes.seasonNumber", equalTo(Integer.parseInt(SEASON_NUMBER))).
                body("data.attributes.homeGoals", nullValue()).
                body("data.attributes.awayGoals", nullValue());
    }

    @Test
    public void shouldCreatePlayedFixture() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String fixtureId = whenCreate(FIXTURE_URL, withPlayedFixture(SEASON_NUMBER, newDivisionId,
                newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1, "1", "0")).
                then().
                statusCode(HttpStatus.SC_CREATED).
                assertThat().
                body("data.id", notNullValue()).
                contentType(ContentType.JSON).extract().path("data.id");
        newFixtureIds.add(fixtureId);

        whenGet(FIXTURE_URL, fixtureId).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(fixtureId)).
                body("data.attributes.fixtureDate", equalTo(FIXTURE_DATE_1)).
                body("data.attributes.divisionId", equalTo(newDivisionId)).
                body("data.attributes.homeTeamId", equalTo(newHomeTeamId)).
                body("data.attributes.awayTeamId", equalTo(newAwayTeamId)).
                body("data.attributes.seasonNumber", equalTo(Integer.parseInt(SEASON_NUMBER))).
                body("data.attributes.homeGoals", equalTo(1)).
                body("data.attributes.awayGoals", equalTo(0));
    }

    @Test
    public void shouldUpdateAFixtureWithADate() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldUpdateAFixtureWithAScore() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldDeleteFixture() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldThrowExceptionWhenGetAllFixtures() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldHaveAHyperlinksToSeasonDivisionsAndTeams() {
        fail("Not implemented this test yet.");
    }
}
