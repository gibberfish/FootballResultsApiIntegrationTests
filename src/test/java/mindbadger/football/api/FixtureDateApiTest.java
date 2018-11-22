package mindbadger.football.api;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * These tests are dependent upon a running API - the details of which are configured in the application.properties
 *
 * This test uses fake data that will be torn-down at the end.
 * Therefore, this test can be run against a 'live' API.
 */
public class FixtureDateApiTest extends AbstractRestAssuredTest {
    @Test
    public void shouldThrowAnErrorWhenAttemptToCreateAFixtureDate() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldGetFixtureDateWhenFixtureAdded() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldGetMultipleFixtureDatesWhenMultipleFixturesWithDifferentDatesAdded() {
        fail("Not implemented this test yet.");
    }

    @Test
    public void shouldHaveAHyperlinksToFixturesAndStatistics() {
        fail("Not implemented this test yet.");
    }
}