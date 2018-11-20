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
public class SeasonDivisionTeamApiTest extends AbstractRestAssuredTest {
    @Test
    public void seasonDivisionTeamsHyperlinkForNewSeasonDivisionShouldReturnNoSeasonDivisionTeams() {
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

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_URL + SEASON_NUMBER +
                "-" + newDivisionId + "/teams";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data", empty());
    }

    @Test
    public void shouldAddNewTeamToSeasonDivision() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");

        newDivisionIds.add(newDivisionId);

        String newTeamId = whenCreate(TEAM_URL, withTeam(TEAM1_NAME)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");

        newTeamIds.add(newTeamId);

        final String SEASON_DIVISION_TEAM_ID = SEASON_NUMBER + "-" + newDivisionId + "-" + newTeamId;

        whenCreate(SEASON_TO_SEASON_DIVISION_URL,
                withSeasonDivision(SEASON_NUMBER, newDivisionId, "1"));

        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(SEASON_NUMBER, newDivisionId, newTeamId)).
                then().
                statusCode(HttpStatus.SC_CREATED).
                assertThat().
                body("data.id", equalTo(SEASON_DIVISION_TEAM_ID));

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_URL + SEASON_NUMBER +
                "-" + newDivisionId + "/teams";

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

        String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");

        newDivisionIds.add(newDivisionId);

        String newTeam1Id = whenCreate(TEAM_URL, withTeam(TEAM1_NAME)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");

        newTeamIds.add(newTeam1Id);

        String newTeam2Id = whenCreate(TEAM_URL, withTeam(TEAM2_NAME)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");

        newTeamIds.add(newTeam2Id);

        final String SEASON_DIVISION_TEAM1_ID = SEASON_NUMBER + "-" + newDivisionId + "-" + newTeam1Id;
        final String SEASON_DIVISION_TEAM2_ID = SEASON_NUMBER + "-" + newDivisionId + "-" + newTeam2Id;

        whenCreate(SEASON_TO_SEASON_DIVISION_URL,
                withSeasonDivision(SEASON_NUMBER, newDivisionId, "1"));

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
                "-" + newDivisionId + "/teams";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.size()", is(2));
    }

    @Test
    public void shouldReturnNotFoundWhenGettingNonExistentSeasonDivisionTeamButSeasonDivisionExists() {
        whenCreate(SEASON_URL, withSeason(SEASON_NUMBER));

        String newDivisionId = whenCreate(DIVISION_URL, withDivision(DIVISION1_NAME)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");

        newDivisionIds.add(newDivisionId);

        String newTeamId = whenCreate(TEAM_URL, withTeam(TEAM1_NAME)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");

        newTeamIds.add(newTeamId);

        final String SEASON_DIVISION_TEAM_ID = SEASON_NUMBER + "-" + newDivisionId + "-" + newTeamId;

        whenCreate(SEASON_TO_SEASON_DIVISION_URL,
                withSeasonDivision(SEASON_NUMBER, newDivisionId, "1"));

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_TEAM_URL + SEASON_NUMBER +
                "-" + newDivisionId + "-NONEXISTENTTEAM";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldReturnNotFoundWhenGettingNonExistentSeasonDivisionTeamWhereSeasonDivisionDoesntExist() {

        final String SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL = SEASON_DIVISION_TEAM_URL + SEASON_NUMBER +
                "-NONEXISTENTDIVISION-NONEXISTENTTEAM";

        whenGet(SEASON_DIVISION_TO_SEASON_DIVISION_TEAM_URL).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

}
