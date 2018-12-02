package mindbadger.football.api;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.MessageCreationHelper.withTeamStatistics;
import static mindbadger.football.api.helpers.OperationHelper.*;
import static mindbadger.football.api.helpers.TestPreConditionHelper.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class TeamStatisticsApiTest extends AbstractRestAssuredTest {

    @Test
    public void shouldGetEmptyTeamStatisticsForAFixtureDate () {
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        whenGet(FIXTURE_DATE_URL + SEASON_NUMBER + ID_SEPARATOR +
                newDivisionId + ID_SEPARATOR + ID_SEPARATOR + FIXTURE_DATE_1 + "/teamStatistics").
                then().
                assertThat().
                body("data.size()", is(2)).
                body("data[0].attributes.statistics.size()", is(0)).
                body("data[1].attributes.statistics.size()", is(0));
    }

    @Test
    public void shouldUpdateTeamStatisticsAtCanonicalUrl() {
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        List<Map<String, Integer>> statistics = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();
        map.put("A",1);
        statistics.add(map);
        map = new HashMap<>();
        map.put("B",2);
        statistics.add(map);

        String expectedTeamStatisticId = SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newHomeTeamId + ID_SEPARATOR + FIXTURE_DATE_1;

        String newTeamStatisticId = whenUpdate(TEAM_STATISTICS_URL, expectedTeamStatisticId, withTeamStatistics(
                SEASON_NUMBER, newDivisionId, newHomeTeamId, FIXTURE_DATE_1, statistics)).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.id", equalTo(expectedTeamStatisticId)).
                body("data.attributes.statistics.size()", is(2)).
                body("data.attributes.statistics.find{it.statistic == 'A'}.value", equalTo(1)).
                body("data.attributes.statistics.find{it.statistic == 'B'}.value", equalTo(2)).
                contentType(ContentType.JSON).extract().path("data.id");

        newTeamStatisticIds.add(newTeamStatisticId);
    }

    @Test
    public void shouldGetTeamStatisticsAtCanonicalUrl () {
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        List<Map<String, Integer>> statistics = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();
        map.put("A",1);
        statistics.add(map);
        map = new HashMap<>();
        map.put("B",2);
        statistics.add(map);

        String newTeamStatisticId = givenATeamStatisticsWith(
                SEASON_NUMBER, newDivisionId, newHomeTeamId, FIXTURE_DATE_1, statistics);
        newTeamStatisticIds.add(newTeamStatisticId);

        whenGet(TEAM_STATISTICS_URL + newTeamStatisticId).
                then().
                assertThat().
                body("data.attributes.statistics.size()", is(2)).
                body("data.attributes.statistics.find{it.statistic == 'A'}.value", equalTo(1)).
                body("data.attributes.statistics.find{it.statistic == 'B'}.value", equalTo(2));
    }

    @Test
    public void shouldDeleteTeamStatisticsAtCanonicalUrl () {
        // Will delete all team statistic records for a date, but not the resource itself
        // (not pure REST!)
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        List<Map<String, Integer>> statistics = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();
        map.put("A",1);
        statistics.add(map);
        map = new HashMap<>();
        map.put("B",2);
        statistics.add(map);

        String newTeamStatisticId = givenATeamStatisticsWith(
                SEASON_NUMBER, newDivisionId, newHomeTeamId, FIXTURE_DATE_1, statistics);
        newTeamStatisticIds.add(newTeamStatisticId);

        whenDelete(TEAM_STATISTICS_URL, newTeamStatisticId).
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);

        whenGet(TEAM_STATISTICS_URL + newTeamStatisticId).
                then().
                assertThat().
                body("data.attributes.statistics.size()", is(0));
    }

    @Test
    public void shouldDeleteOnlySomeOfTheTeamStatisticsAtCanonicalUrl () {
        // Will delete all team statistic records for a date, but not the resource itself
        // (not pure REST!)
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        List<Map<String, Integer>> statistics = new ArrayList<>();
        Map<String,Integer> map1 = new HashMap<>();
        map1.put("A",1);
        statistics.add(map1);
        Map<String,Integer> map2 = new HashMap<>();
        map2.put("B",2);
        statistics.add(map2);
        Map<String,Integer> map3 = new HashMap<>();
        map3.put("C",3);
        statistics.add(map3);
        Map<String,Integer> map4 = new HashMap<>();
        map4.put("D",4);
        statistics.add(map4);

        String newTeamStatisticId = givenATeamStatisticsWith(
                SEASON_NUMBER, newDivisionId, newHomeTeamId, FIXTURE_DATE_1, statistics);
        newTeamStatisticIds.add(newTeamStatisticId);

        statistics.remove(map1);
        statistics.remove(map2);

        whenUpdate(TEAM_STATISTICS_URL, newTeamStatisticId, withTeamStatistics(
                SEASON_NUMBER, newDivisionId, newHomeTeamId, FIXTURE_DATE_1, statistics)).
                then().
                statusCode(HttpStatus.SC_OK).
                assertThat().
                body("data.attributes.statistics.size()", is(2)).
                body("data.attributes.statistics.find{it.statistic == 'C'}.value", equalTo(3)).
                body("data.attributes.statistics.find{it.statistic == 'D'}.value", equalTo(4))
        ;

        whenGet(TEAM_STATISTICS_URL + newTeamStatisticId).
                then().
                assertThat().
                body("data.attributes.statistics.size()", is(2)).
                body("data.attributes.statistics.find{it.statistic == 'C'}.value", equalTo(3)).
                body("data.attributes.statistics.find{it.statistic == 'D'}.value", equalTo(4));
    }

    @Test
    public void shouldGetExistingTeamStatisticsForAFixtureDate () {
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        List<Map<String, Integer>> statistics = new ArrayList<>();
        Map<String,Integer> map1 = new HashMap<>();
        map1.put("A",1);
        statistics.add(map1);
        Map<String,Integer> map2 = new HashMap<>();
        map2.put("B",2);
        statistics.add(map2);

        String newTeamStatisticId = givenATeamStatisticsWith(
                SEASON_NUMBER, newDivisionId, newHomeTeamId, FIXTURE_DATE_1, statistics);
        newTeamStatisticIds.add(newTeamStatisticId);

        String emptyTeamStatisticId =
                SEASON_NUMBER + ID_SEPARATOR + newDivisionId +
                        ID_SEPARATOR + newAwayTeamId + ID_SEPARATOR + FIXTURE_DATE_1;

        whenGet(FIXTURE_DATE_URL + SEASON_NUMBER + ID_SEPARATOR +
                newDivisionId + ID_SEPARATOR + ID_SEPARATOR + FIXTURE_DATE_1 + "/teamStatistics").
            then().assertThat().
            body("data.find{it.id == '" + newTeamStatisticId + "'}.attributes.statistics.size()", equalTo(2)).
            body("data.find{it.id == '" + newTeamStatisticId + "'}.attributes.statistics.find{it.statistic == 'A'}.value", equalTo(1)).
            body("data.find{it.id == '" + newTeamStatisticId + "'}.attributes.statistics.find{it.statistic == 'B'}.value", equalTo(2)).
            body("data.find{it.id == '" + emptyTeamStatisticId + "'}.attributes.statistics.size()", equalTo(0));
    }

    @Test
    public void shouldReturn501WhenCreateTeamStatisticsForAFixtureDate() {
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        List<Map<String, Integer>> statistics = new ArrayList<>();
        Map<String,Integer> map1 = new HashMap<>();
        map1.put("A",1);
        statistics.add(map1);
        Map<String,Integer> map2 = new HashMap<>();
        map2.put("B",2);
        statistics.add(map2);

        whenCreate(FIXTURE_DATE_URL + SEASON_NUMBER + ID_SEPARATOR +
                newDivisionId + ID_SEPARATOR + ID_SEPARATOR + FIXTURE_DATE_1 + "/teamStatistics",
                withTeamStatistics(SEASON_NUMBER, newDivisionId, newHomeTeamId,
                FIXTURE_DATE_1, statistics)).
                then().
                statusCode(HttpStatus.SC_NOT_IMPLEMENTED);
    }

    @Test
    public void shouldReturn501WhenCreateTeamStatisticsAtCanonicalUrl() {
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        List<Map<String, Integer>> statistics = new ArrayList<>();
        Map<String,Integer> map1 = new HashMap<>();
        map1.put("A",1);
        statistics.add(map1);
        Map<String,Integer> map2 = new HashMap<>();
        map2.put("B",2);
        statistics.add(map2);

        whenCreate(TEAM_STATISTICS_URL, withTeamStatistics(SEASON_NUMBER, newDivisionId, newHomeTeamId,
                FIXTURE_DATE_1, statistics)).
                then().
                statusCode(HttpStatus.SC_NOT_IMPLEMENTED);

    }

    @Test
    public void shouldReturn404WhenCanonicalTeamStatisticsNotFound() {
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

        String newFixtureId = givenAFixtureWith(SEASON_NUMBER, newDivisionId, newHomeTeamId, newAwayTeamId, FIXTURE_DATE_1);
        newFixtureIds.add(newFixtureId);

        List<Map<String, Integer>> statistics = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();
        map.put("A",1);
        statistics.add(map);
        map = new HashMap<>();
        map.put("B",2);
        statistics.add(map);

        String newTeamStatisticId = givenATeamStatisticsWith(
                SEASON_NUMBER, newDivisionId, newHomeTeamId, FIXTURE_DATE_1, statistics);
        newTeamStatisticIds.add(newTeamStatisticId);

        String teamStatisticIdNotFoundId =
                SEASON_NUMBER + ID_SEPARATOR + newDivisionId + ID_SEPARATOR + newHomeTeamId + ID_SEPARATOR + FIXTURE_DATE_2;

        whenGet(TEAM_STATISTICS_URL + teamStatisticIdNotFoundId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

}
