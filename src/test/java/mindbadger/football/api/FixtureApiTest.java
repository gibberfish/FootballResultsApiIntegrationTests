package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import mindbadger.football.api.helpers.MessageCreationHelper;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.*;
import static mindbadger.football.api.helpers.OperationHelper.*;
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
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String fixtureId = whenCreate(FIXTURE_URL, withFixture(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, null)).
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
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String fixtureId = whenCreate(FIXTURE_URL, MessageCreationHelper.withFixture(SEASON_NUMBER, newDivisionId,
                newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1, null)).
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
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String fixtureId = whenCreate(FIXTURE_URL, MessageCreationHelper.withFixture(SEASON_NUMBER, newDivisionId,
                newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1, "1", "0", null)).
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
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, newDivisionId, newHomeTeamId,
                newAwayTeamId, FIXTURE_DATE_1, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(newFixtureId)).
                body("data.attributes.fixtureDate", equalTo(FIXTURE_DATE_1));
    }

    @Test
    public void shouldUpdateAFixtureWithAScore() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId,
                FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, newDivisionId, newHomeTeamId,
                newAwayTeamId, FIXTURE_DATE_1, "3", "0", newFixtureId)).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(newFixtureId)).
                body("data.attributes.homeGoals", equalTo(Integer.parseInt("3"))).
                body("data.attributes.awayGoals", equalTo(Integer.parseInt("0")));
    }

    @Test
    public void shouldUpdateAFixtureToRemoveDateAndScore() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId,
                FIXTURE_DATE_1, "3", "0");
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, newDivisionId, newHomeTeamId,
                newAwayTeamId, null, null, null, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(newFixtureId)).
                body("data.attributes.homeGoals", nullValue()).
                body("data.attributes.awayGoals", nullValue()).
                body("data.attributes.fixtureDate", nullValue());

        whenGet(FIXTURE_URL, newFixtureId).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(newFixtureId)).
                body("data.attributes.fixtureDate", nullValue()).
                body("data.attributes.divisionId", equalTo(newDivisionId)).
                body("data.attributes.homeTeamId", equalTo(newHomeTeamId)).
                body("data.attributes.awayTeamId", equalTo(newAwayTeamId)).
                body("data.attributes.seasonNumber", equalTo(Integer.parseInt(SEASON_NUMBER))).
                body("data.attributes.homeGoals", nullValue()).
                body("data.attributes.awayGoals", nullValue());

    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateAScoreWithoutADate() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, newDivisionId, newHomeTeamId,
                newAwayTeamId, null, "3", "0", newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateWithANonExistentSeason() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(NON_EXISTENT_SEASON_NUM, newDivisionId, newHomeTeamId,
                newAwayTeamId, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateWithADifferentSeason() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        givenASeason(SEASON_NUMBER_2);

        givenASeasonDivisionWith(SEASON_NUMBER_2, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER_2, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER_2, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER_2, newDivisionId, newHomeTeamId,
                newAwayTeamId, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateWithANonExistentDivision() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, NON_EXISTENT_DIVISION_ID, newHomeTeamId,
                newAwayTeamId, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateWithADifferentDivision() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newDivisionId2 = givenADivisionWithName(DIVISION2_NAME);
        newDivisionIds.add(newDivisionId2);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId2, "2");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId2, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId2, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER_2, newDivisionId, newHomeTeamId,
                newAwayTeamId, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateWithANonExistentHomeTeam() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, newDivisionId, NON_EXISTENT_TEAM_ID,
                newAwayTeamId, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateWithADifferentHomeTeam() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newTeamId2 = givenATeamWithName(TEAM3_NAME);
        newTeamIds.add(newTeamId2);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newTeamId2);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, newDivisionId, newTeamId2,
                newAwayTeamId, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateWithANonExistentAwayTeam() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, newDivisionId, newHomeTeamId,
                NON_EXISTENT_TEAM_ID, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldThrowAnExceptionWhenAttemptToUpdateWithADifferentAwayTeam() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newTeamId2 = givenATeamWithName(TEAM3_NAME);
        newTeamIds.add(newTeamId2);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newTeamId2);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId);
        newFixtureIds.add(newFixtureId);

        whenUpdate(FIXTURE_URL, newFixtureId, withFixture(SEASON_NUMBER, newDivisionId, newHomeTeamId,
                newTeamId2, newFixtureId)).
                then().
                statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldDeleteFixture() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId,
                FIXTURE_DATE_1, "3", "0");
        newFixtureIds.add(newFixtureId);

        whenDelete(FIXTURE_URL, newFixtureId).
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);

        whenGet(FIXTURE_URL, newFixtureId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldReturnNotFoundWhenGetFixtureWithNonExistentId() {
        whenGet(FIXTURE_URL, NON_EXISTENT_FIXTURE_ID).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldThrowExceptionWhenGetAllFixtures() {
        whenGet(FIXTURE_URL).
                then().
                statusCode(HttpStatus.SC_NOT_IMPLEMENTED);
    }

    @Test
    public void shouldHaveAHyperlinksToSeasonDivisionsAndTeams() {
        givenASeason(SEASON_NUMBER);

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newHomeTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newHomeTeamId);

        String newAwayTeamId = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newAwayTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newHomeTeamId);

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newAwayTeamId);

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId,
                FIXTURE_DATE_1, "3", "0");
        newFixtureIds.add(newFixtureId);


        String seasonDivisionHyperlink = host + ":" + port + basePath +
                FIXTURE_URL + newFixtureId + "/seasonDivision";

        String homeTeamHyperlink = host + ":" + port + basePath +
                FIXTURE_URL + newFixtureId + "/homeTeam";

        String awayTeamHyperlink = host + ":" + port + basePath +
                FIXTURE_URL + newFixtureId + "/awayTeam";

        whenGet(FIXTURE_URL + newFixtureId).
                then().
                assertThat().
                body("data.relationships.seasonDivision.links.related", equalTo(seasonDivisionHyperlink)).
                body("data.relationships.homeTeam.links.related", equalTo(homeTeamHyperlink)).
                body("data.relationships.awayTeam.links.related", equalTo(awayTeamHyperlink));
    }

}
