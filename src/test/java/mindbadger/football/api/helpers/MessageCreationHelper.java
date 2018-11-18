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

    public static JsonObject createBaseMessage () {
        JsonObject message = new JsonObject();
        JsonObject data = new JsonObject();
        JsonObject attributes = new JsonObject();
        message.add("data", data);
        data.add("attributes", attributes);
        return message;
    }

}