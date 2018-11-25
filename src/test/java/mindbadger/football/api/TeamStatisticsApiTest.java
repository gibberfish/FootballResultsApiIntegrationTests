package mindbadger.football.api;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class TeamStatisticsApiTest extends AbstractRestAssuredTest {

    @Test
    public void shouldGetEmptyTeamStatisticsForAFixtureDate () {
        //This should work even if there are no team statistics
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldReturn501WhenCreateTeamStatisticsForAFixtureDate() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldReturn422WhenCreateTeamStatisticsAtCanonicalUrlWithoutId() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldCreateTeamStatisticsAtCanonicalUrl() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldGetTeamStatisticsAtCanonicalUrl () {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldDeleteTeamStatisticsAtCanonicalUrl () {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldGetExistingTeamStatisticsForAFixtureDate () {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldReturn404WhenCanonicalTeamStatisticsNotFound() {
        fail("Not implemented this test yet.");
    }

}
