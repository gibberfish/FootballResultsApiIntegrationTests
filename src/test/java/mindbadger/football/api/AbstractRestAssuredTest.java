package mindbadger.football.api;

import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static mindbadger.football.api.ApiTestConstants.*;
import static mindbadger.football.api.helpers.OperationHelper.whenDelete;

public class AbstractRestAssuredTest {
    protected String port;
    protected String basePath;
    protected String host;

    protected Set<String> newDivisionIds = new HashSet<>();
    protected Set<String> newTeamIds = new HashSet<>();
    protected Set<String> newFixtureIds = new HashSet<>();
    protected Set<String> newTeamStatisticIds = new HashSet<>();
    protected Set<String> newDivisionMappingIds = new HashSet<>();
    protected Set<String> newTeamMappingIds = new HashSet<>();

    @Before
    public void setup() throws IOException {
        Properties prop = new Properties();
        prop.load(SeasonApiTest.class.getClassLoader().getResourceAsStream("application.properties"));

        port = prop.getProperty("server.port");
        RestAssured.port = Integer.valueOf(port);

        basePath = prop.getProperty("server.base");
        RestAssured.basePath = basePath;

        host = prop.getProperty("server.host");
        RestAssured.baseURI = host;
    }

    @After
    public void deleteTestData() {
        for (String teamStatisticId : newTeamStatisticIds) {
            whenDelete(TEAM_STATISTICS_URL, teamStatisticId);
        }
        for (String fixtureId : newFixtureIds) {
            whenDelete(FIXTURE_URL, fixtureId);
        }
        whenDelete(SEASON_URL, SEASON_NUMBER);
        whenDelete(SEASON_URL, SEASON_NUMBER_2);
        for (String divisionId : newDivisionIds) {
            whenDelete(DIVISION_URL, divisionId);
        }
        for (String teamId : newTeamIds) {
            whenDelete(TEAM_URL, teamId);
        }
        for (String divisionMappingId : newDivisionMappingIds) {
            whenDelete(DIVISION_MAPPING_URL, divisionMappingId);
        }
        for (String teamMappingId : newTeamMappingIds) {
            whenDelete(TEAM_MAPPING_URL, teamMappingId);
        }
    }
}
