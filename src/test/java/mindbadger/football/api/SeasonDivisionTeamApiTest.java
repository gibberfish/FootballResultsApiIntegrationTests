package mindbadger.football.api;

import org.apache.http.HttpStatus;
import org.junit.Test;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.withSeason;
import static mindbadger.football.api.helpers.MessageCreationHelper.withSeasonDivisionTeam;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static mindbadger.football.api.helpers.TestPreConditionHelper.*;
import static org.hamcrest.Matchers.*;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class SeasonDivisionTeamApiTest extends AbstractRestAssuredTest {
    @Test
    public void seasonDivisionTeamsHyperlinkForNewSeasonDivisionShouldReturnNoSeasonDivisionTeams() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_URL + SEASON_NUMBER +
                ID_SEPARATOR + newDivisionId + "/teams";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data", empty());
    }

    @Test
    public void shouldAddNewTeamToSeasonDivision() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newTeamId);

        final String SEASON_DIVISION_TEAM_ID = SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newTeamId;

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(SEASON_NUMBER, newDivisionId, newTeamId)).
                then().
                statusCode(HttpStatus.SC_CREATED).
                assertThat().
                body("data.id", equalTo(SEASON_DIVISION_TEAM_ID));

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_URL + SEASON_NUMBER +
                ID_SEPARATOR + newDivisionId + "/teams";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.size()", is(1)).
                body("data[0].id", equalTo(SEASON_DIVISION_TEAM_ID));
    }

    @Test
    public void shouldAddMultipleNewTeamsToSeasonDivision() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newTeam1Id = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newTeam1Id);

        String newTeam2Id = givenATeamWithName(TEAM2_NAME);
        newTeamIds.add(newTeam2Id);

        final String SEASON_DIVISION_TEAM1_ID = SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newTeam1Id;
        final String SEASON_DIVISION_TEAM2_ID = SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newTeam2Id;

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(SEASON_NUMBER, newDivisionId, newTeam1Id)).
                then().
                statusCode(HttpStatus.SC_CREATED).
                assertThat().
                body("data.id", equalTo(SEASON_DIVISION_TEAM1_ID));

        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(SEASON_NUMBER, newDivisionId, newTeam2Id)).
                then().
                statusCode(HttpStatus.SC_CREATED).
                assertThat().
                body("data.id", equalTo(SEASON_DIVISION_TEAM2_ID));

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_URL + SEASON_NUMBER +
                ID_SEPARATOR + newDivisionId + "/teams";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.size()", is(2));
    }

    @Test
    public void shouldReturnNotFoundWhenGettingNonExistentSeasonDivisionTeamButSeasonDivisionExists() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_TEAM_URL + SEASON_NUMBER +
                ID_SEPARATOR + newDivisionId + ID_SEPARATOR + "NONEXISTENTTEAM";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldReturnNotFoundWhenGettingNonExistentSeasonDivisionTeamWhereSeasonDivisionDoesntExist() {

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_TEAM_URL + SEASON_NUMBER +
                ID_SEPARATOR + "NONEXISTENTDIVISION" + ID_SEPARATOR + "NONEXISTENTTEAM";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldDeleteASeasonDivisionTeam() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newTeamId);

        final String SEASON_DIVISION_TEAM_ID = SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newTeamId;

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newTeamId);

        whenDelete(SEASON_DIVISION_TEAM_URL, SEASON_DIVISION_TEAM_ID).
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);

        whenGet(SEASON_DIVISION_TEAM_URL, SEASON_DIVISION_TEAM_ID).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldThrowAnErrorWhenAttemptToCreateADuplicateSeasonDivisionTeam() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newTeamId);

        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(SEASON_NUMBER, newDivisionId, newTeamId)).
                then().
                statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    public void shouldThrowAnErrorWhenAttemptToCreateASeasonDivisionTeamWithANonExistentDivision() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newTeamId);

        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(SEASON_NUMBER, NON_EXISTENT_DIVISION_ID, newTeamId)).
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldThrowAnErrorWhenAttemptToCreateASeasonDivisionTeamWithANonExistentSeason() {
        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newTeamId);

        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(NON_EXISTENT_SEASON_NUM, newDivisionId, newTeamId)).
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldThrowAnErrorWhenAttemptToCreateASeasonDivisionTeamWithANonExistentTeam() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(SEASON_NUMBER, newDivisionId, NON_EXISTENT_TEAM_ID)).
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldHaveAHyperlinksToSeasonDivisionAndTeam() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = givenADivisionWithName(DIVISION1_NAME);
        newDivisionIds.add(newDivisionId);

        String newTeamId = givenATeamWithName(TEAM1_NAME);
        newTeamIds.add(newTeamId);

        givenASeasonDivisionWith(SEASON_NUMBER, newDivisionId, "1");

        givenASeasonDivisionTeamWith(SEASON_NUMBER, newDivisionId, newTeamId);

        String seasonDivisionHyperlink = host + ":" + port + basePath +
                SEASON_DIVISION_TEAM_URL + SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newTeamId + "/seasonDivision";

        String teamHyperlink = host + ":" + port + basePath +
                SEASON_DIVISION_TEAM_URL + SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newTeamId + "/team";

        whenGet(SEASON_DIVISION_TEAM_URL + SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newTeamId).
                then().
                assertThat().
                body("data.relationships.seasonDivision.links.related", equalTo(seasonDivisionHyperlink)).
                body("data.relationships.team.links.related", equalTo(teamHyperlink));
    }
}
