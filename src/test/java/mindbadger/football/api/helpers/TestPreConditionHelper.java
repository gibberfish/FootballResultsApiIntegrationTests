package mindbadger.football.api.helpers;

import com.jayway.restassured.http.ContentType;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.*;
import static mindbadger.football.api.helpers.OperationHelper.whenCreate;

public class TestPreConditionHelper {
    public static void givenASeason (String seasonNumber) {
        whenCreate(SEASON_URL, withSeason(seasonNumber));
    }

    public static String givenADivisionWithName (String name) {
        return whenCreate(DIVISION_URL, withDivision(name)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");
    }

    public static String givenATeamWithName (String name) {
        return whenCreate(TEAM_URL, withTeam(name)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");
    }

    public static void givenASeasonDivisionWith (String seasonNumber, String divisionId, String position) {
        whenCreate(SEASON_DIVISION_URL, withSeasonDivision(SEASON_NUMBER, divisionId, "1"));
    }

    public static void givenASeasonDivisionTeamWith (String seasonNumber, String divisionId, String teamName) {
        whenCreate(SEASON_DIVISION_TEAM_URL, withSeasonDivisionTeam(seasonNumber, divisionId, teamName));
    }

    public static String givenAFixtureWith (String seasonNumber, String divisionId, String homeTeamId,
                                            String awayTeamId) {
        return whenCreate(FIXTURE_URL, MessageCreationHelper.withFixture(seasonNumber, divisionId,
                homeTeamId, awayTeamId,null)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");
    }

    public static String givenAFixtureWith (String seasonNumber, String divisionId, String homeTeamId,
                                            String awayTeamId, String fixtureDate) {
        return whenCreate(FIXTURE_URL, MessageCreationHelper.withFixture(seasonNumber, divisionId,
                homeTeamId, awayTeamId, fixtureDate, null)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");
    }

    public static String givenAFixtureWith (String seasonNumber, String divisionId, String homeTeamId,
                                          String awayTeamId, String fixtureDate, String homeGoals, String awayGoals) {
        return whenCreate(FIXTURE_URL, MessageCreationHelper.withFixture(seasonNumber, divisionId,
                homeTeamId, awayTeamId, fixtureDate, homeGoals, awayGoals, null)).
                then().
                contentType(ContentType.JSON).extract().path("data.id");
    }

}
