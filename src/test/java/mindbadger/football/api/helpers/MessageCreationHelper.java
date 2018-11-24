package mindbadger.football.api.helpers;

import com.google.gson.JsonObject;

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

    public static JsonObject createBaseMessage () {
        JsonObject message = new JsonObject();
        JsonObject data = new JsonObject();
        JsonObject attributes = new JsonObject();
        message.add("data", data);
        data.add("attributes", attributes);
        return message;
    }

    public static JsonObject withFixtureDate(String seasonNumber, String divisionId, String fixtureDate) {
        JsonObject message = MessageCreationHelper.createBaseMessage();
        message.getAsJsonObject("data").addProperty("type", "fixtureDates");
        message.getAsJsonObject("data").addProperty("id", seasonNumber + "-" + divisionId + "_" + fixtureDate);
        message.getAsJsonObject("data").getAsJsonObject("attributes").addProperty("fixtureDate", fixtureDate);
        return message;
    }
}
