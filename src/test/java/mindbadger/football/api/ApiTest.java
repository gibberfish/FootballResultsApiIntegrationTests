package mindbadger.football.api;

import static org.junit.Assert.*;

import io.crnk.client.CrnkClient;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.repository.ResourceRepositoryV2;
import mindbadger.football.api.repository.CrnkFixtureRepository;
import mindbadger.football.api.repository.CrnkSeasonDivisionFixtureDateRepository;
import mindbadger.football.api.repository.CrnkTeamStatisticsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import mindbadger.football.api.model.*;

import mindbadger.TestApplication;

import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestApplication.class})
public class ApiTest {
	private static final Integer SEASON_NUMBER = new Integer(1700);
	private static final String DIVISION1_NAME = "Madeup Division 1";
	private static final String DIVISION2_NAME = "Madeup Division 2";
	private static final String TEAM1_NAME = "Madeup Team 1";
	private static final String TEAM2_NAME = "Madeup Team 2";
	private static final String TEAM3_NAME = "Madeup Team 3";

	@Value("${api.url}")
	private String apiUrl;

	private CrnkClient client;
	private ResourceRepositoryV2<CrnkSeason, Integer> seasonRepository;
	private ResourceRepositoryV2<CrnkDivision,String> divisionRepository;
	private ResourceRepositoryV2<CrnkTeam, String> teamRepository;
	private ResourceRepositoryV2<CrnkSeasonDivision, String> seasonDivisionRepository;
	private ResourceRepositoryV2<CrnkSeasonDivisionTeam, String> seasonDivisionTeamRepository;
	private ResourceRepositoryV2<CrnkSeasonDivisionFixtureDate,String> seasonDivisionFixtureDateRepository;
	private ResourceRepositoryV2<CrnkTeamStatisticsRepository, String> teamStatisticsRepository;
	private ResourceRepositoryV2<CrnkFixtureRepository, String> fixtureRepository;

	@Before
	public void setup() {
		client = new CrnkClient(apiUrl);

		seasonRepository = client.getRepositoryForType(CrnkSeason.class);
		divisionRepository = client.getRepositoryForType(CrnkDivision.class);
		teamRepository = client.getRepositoryForType(CrnkTeam.class);
		seasonDivisionRepository = client.getRepositoryForType(CrnkSeasonDivision.class);
		seasonDivisionTeamRepository = client.getRepositoryForType(CrnkSeasonDivisionTeam.class);
		seasonDivisionFixtureDateRepository = client.getRepositoryForType(CrnkSeasonDivisionFixtureDate.class);
		teamStatisticsRepository = client.getRepositoryForType(CrnkTeamStatisticsRepository.class);
		fixtureRepository = client.getRepositoryForType(CrnkFixtureRepository.class);


	}

	@After
	public void tearDown() {
		CrnkSeason season = seasonRepository.findOne(SEASON_NUMBER, new QuerySpec(CrnkSeason.class));
		if (season != null) {
			seasonRepository.delete(season.getId());
		}
	}

	@Test
	public void test () {
		// Given

		// When
		CrnkSeason crnkSeason = seasonRepository.findOne(SEASON_NUMBER, new QuerySpec(CrnkSeason.class));

		// Then
		assertNull(crnkSeason);

		// Given
		crnkSeason = new CrnkSeason();
		crnkSeason.getSeason().setSeasonNumber(SEASON_NUMBER);

		// When
		CrnkSeason createdCrnkSeason = seasonRepository.create(crnkSeason);

		// Then
		assertEquals (SEASON_NUMBER, createdCrnkSeason.getSeasonNumber());

	}

}
