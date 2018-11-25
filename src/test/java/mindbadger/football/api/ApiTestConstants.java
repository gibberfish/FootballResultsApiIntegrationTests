package mindbadger.football.api;

public class ApiTestConstants {
    public static final String SEASON_NUMBER = "1750";
    public static final String SEASON_NUMBER_2 = "1751";
    public static final String DIVISION1_NAME = "Madeup Division 1";
    public static final String DIVISION2_NAME = "Madeup Division 2";
    public static final String TEAM1_NAME = "Madeup Team 1";
    public static final String TEAM2_NAME = "Madeup Team 2";
    public static final String TEAM3_NAME = "Madeup Team 3";
    public static final String TEAM4_NAME = "Madeup Team 4";
    public static final String FIXTURE_DATE_1 = "1750-10-08";
    public static final String FIXTURE_DATE_2 = "1750-10-12";

    public static final String NON_EXISTENT_SEASON_NUM = "1700";
    public static final String NON_EXISTENT_DIVISION_ID = "NoSuchID";
    public static final String NON_EXISTENT_TEAM_ID = "NoSuchID";
    public static final String NON_EXISTENT_FIXTURE_ID = "NoSuchID";

    public static final String SEASON_URL = "seasons/";
    public static final String DIVISION_URL = "divisions/";
    public static final String TEAM_URL = "teams/";
    public static final String SEASON_DIVISION_URL = "seasonDivisions/";
    public static final String SEASON_DIVISION_TEAM_URL = "seasonDivisionTeams/";
    public static final String FIXTURE_URL = "fixtures/";
    public static final String FIXTURE_DATE_URL = "fixtureDates/";
    public static final String TEAM_STATISTICS_URL = "teamStatistics/";

    public static final String ID_SEPARATOR = "_";

    public static final String SEASON_TO_SEASON_DIVISION_URL = SEASON_URL + SEASON_NUMBER + "/seasonDivisions";
    public static final String SEASON_TO_NON_EXISTENT_SEASON_DIVISION_URL = SEASON_URL + "1700/seasonDivisions";

}
