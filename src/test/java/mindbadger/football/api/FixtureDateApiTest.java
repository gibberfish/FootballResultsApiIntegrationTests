package mindbadger.football.api;

import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.withFixtureDate;
import static mindbadger.football.api.helpers.OperationHelper.whenCreate;
import static mindbadger.football.api.helpers.OperationHelper.whenGet;
import static mindbadger.football.api.helpers.TestPreConditionHelper.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class FixtureDateApiTest extends AbstractRestAssuredTest {

    @Test
    public void shouldThrowAnErrorWhenAttemptToGetANonExistentFixtureDate() {
        final String fixtureDateId =
                SEASON_NUMBER + ID_SEPARATOR + NON_EXISTENT_DIVISION_ID + ID_SEPARATOR + ID_SEPARATOR + FIXTURE_DATE_1;

        whenGet(FIXTURE_DATE_URL + fixtureDateId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldThrowAnErrorWhenAttemptToCreateAFixtureDate() {
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

        final String fixtureDateId =
                SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + ID_SEPARATOR + FIXTURE_DATE_1;

        whenCreate(FIXTURE_DATE_URL, withFixtureDate(SEASON_NUMBER, newDivisionId, FIXTURE_DATE_1)).
                then().
                statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldGetCanonicalFixtureDateWhenFixtureAdded() {
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

        final String fixtureDateId =
                SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + ID_SEPARATOR + FIXTURE_DATE_1;

        whenGet(FIXTURE_DATE_URL + fixtureDateId).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(fixtureDateId)).
                body("data.attributes.fixtureDate", equalTo(FIXTURE_DATE_1));
    }

    @Test
    public void shouldGetSeasonDivisionFixtureDateWhenFixtureAdded() {
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

        final String seasonDivisionId = SEASON_NUMBER + ID_SEPARATOR + newDivisionId;
        final String fixtureDateId =
                SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + ID_SEPARATOR + FIXTURE_DATE_1;

        whenGet(SEASON_DIVISION_URL + seasonDivisionId + "/fixtureDates").
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.size()", is(1)).
                body("data[0].id", equalTo(fixtureDateId));
    }

    @Test
    public void shouldGetMultipleFixtureDatesWhenMultipleFixturesWithDifferentDatesAdded() {
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

        String newFixtureId1 = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId,
                FIXTURE_DATE_1, "3", "0");
        newFixtureIds.add(newFixtureId1);

        String newFixtureId2 = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId,
                FIXTURE_DATE_2, "1", "1");
        newFixtureIds.add(newFixtureId2);

        final String seasonDivisionId = SEASON_NUMBER + ID_SEPARATOR + newDivisionId;

        whenGet(SEASON_DIVISION_URL + seasonDivisionId + "/fixtureDates").
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.size()", is(2));
    }

    @Test
    public void shouldHaveAHyperlinksToFixturesAndStatistics() {
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

        final String fixtureDateId =
                SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + ID_SEPARATOR + FIXTURE_DATE_1;

        final String teamStatisticsHyperlink = host + ":" + port + basePath +
                FIXTURE_DATE_URL + fixtureDateId + "/teamStatistics";
        final String fixturesHyperlink = host + ":" + port + basePath +
                FIXTURE_DATE_URL + fixtureDateId + "/fixtures";

        whenGet(FIXTURE_DATE_URL + fixtureDateId).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.relationships.teamStatistics.links.related", equalTo(teamStatisticsHyperlink)).
                body("data.relationships.fixtures.links.related", equalTo(fixturesHyperlink));
    }
}
