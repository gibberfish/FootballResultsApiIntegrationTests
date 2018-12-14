package mindbadger.football.api.helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.util.List;
import java.util.Map;

import static mindbadger.football.api.ApiTestConstants.ID_SEPARATOR;

public class MessageCreationHelper {
    public static JsonObject withSeason (String seasonNumber) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "seasons");
        message.getAsJsonObject("data").addProperty("id", seasonNumber);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("seasonNumber", seasonNumber);
        return message;
    }

    public static JsonObject withDivision (String divisionName) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "divisions");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("divisionName", divisionName);
        return message;
    }

    public static JsonObject withTeam (String teamName) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "teams");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("teamName", teamName);
        return message;
    }

    public static JsonObject withSeasonDivision (String seasonNumber, String divisionId, String position) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "seasonDivisions");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("position", position);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("seasonNumber", seasonNumber);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("divisionId", divisionId);
        return message;
    }

    public static JsonObject withSeasonDivisionTeam (String seasonNumber, String divisionId, String teamId) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "seasonDivisionTeams");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("teamId", teamId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("seasonNumber", seasonNumber);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("divisionId", divisionId);
        return message;
    }

    public static JsonObject withFixture(String seasonNumber, String divisionId, String homeTeamId,
                                         String awayTeamId, String fixtureId) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "fixtures");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("seasonNumber", seasonNumber);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("divisionId", divisionId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("homeTeamId", homeTeamId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("awayTeamId", awayTeamId);
        if (fixtureId != null) {
            message.getAsJsonObject("data").addProperty("id", fixtureId);
        }
        return message;
    }

    public static JsonObject withFixture(String seasonNumber, String divisionId, String homeTeamId,
                                         String awayTeamId, String fixtureDate, String fixtureId) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "fixtures");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("seasonNumber", seasonNumber);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("divisionId", divisionId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("homeTeamId", homeTeamId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("awayTeamId", awayTeamId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("fixtureDate", fixtureDate);
        if (fixtureId != null) {
            message.getAsJsonObject("data").addProperty("id", fixtureId);
        }
        return message;
    }

    public static JsonObject withFixture(String seasonNumber, String divisionId, String homeTeamId,
                                         String awayTeamId, String fixtureDate, String homeGoals, String awayGoals,
                                         String fixtureId) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "fixtures");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("seasonNumber", seasonNumber);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("divisionId", divisionId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("homeTeamId", homeTeamId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("awayTeamId", awayTeamId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("fixtureDate", fixtureDate);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("homeGoals", homeGoals);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("awayGoals",awayGoals);
        if (fixtureId != null) {
            message.getAsJsonObject("data").addProperty("id", fixtureId);
        }
        return message;
    }

    public static JsonObject withFixtureDate(String seasonNumber, String divisionId, String fixtureDate) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "fixtureDates");
        message.getAsJsonObject("data").addProperty("id",
                seasonNumber + ID_SEPARATOR + divisionId + ID_SEPARATOR + ID_SEPARATOR + fixtureDate);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("fixtureDate", fixtureDate);
        return message;
    }

    public static JsonObject withTeamStatistics(String seasonNumber, String divisionId, String teamId,
                                                String fixtureDate, List<Map<String,Integer>> statistics) {
        JsonObject message = MessageCreationHelper.createBaseMessage();

        String id = seasonNumber + ID_SEPARATOR + divisionId + ID_SEPARATOR + teamId + ID_SEPARATOR + fixtureDate;

        message.getAsJsonObject("data").addProperty("type", "teamStatistics");
        message.getAsJsonObject("data").addProperty("id", id);

        JsonArray statisticsArray  = new JsonArray();

        statistics.forEach((statistic) -> {
            JsonObject statisticsObject = new JsonObject();
            statistic.forEach((k,v)-> {
                statisticsObject.addProperty("statistic",k);
                statisticsObject.addProperty("value",v);
            });
            statisticsArray.add(statisticsObject);
        });

        message.getAsJsonObject("data").getAsJsonObject("attributes").add("statistics", statisticsArray);

        return message;
    }

    public static JsonObject withDivisionMapping (String dialect, String sourceId, String fraId) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "division_mapping");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("dialect", dialect);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("sourceId", sourceId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("fraId", fraId);
        return message;
    }

    public static JsonObject withTeamMapping (String dialect, String sourceId, String fraId) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "team_mapping");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("dialect", dialect);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("sourceId", sourceId);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("fraId", fraId);
        return message;
    }

    public static JsonObject withTrackedDivision (String dialect, String sourceId) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "tracked_division");
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("dialect", dialect);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("sourceId", sourceId);
        return message;
    }

    public static JsonObject createBaseMessage () {
        JsonObject message = new JsonObject();
        JsonObject data = new JsonObject();
        JsonObject attributes = new JsonObject();
        message.add("data", data);
        data.add("attributes", attributes);
        return message;
    }
}
