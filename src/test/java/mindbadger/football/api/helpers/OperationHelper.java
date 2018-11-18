package mindbadger.football.api.helpers;

import com.google.gson.JsonObject;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;

public class OperationHelper {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CRNK_CONTENT_TYPE = "application/vnd.api+json";

    public static Response whenDelete(String url, String id) {
        return
                given().
                        header(CONTENT_TYPE_HEADER, CRNK_CONTENT_TYPE).
                        when().
                        delete(url + id);
    }

    public static Response whenGet(String url, String id) {
        return
                given().
                        header(CONTENT_TYPE_HEADER, CRNK_CONTENT_TYPE).
                        when().
                        get(url + id);
    }

    public static Response whenCreate(String url, JsonObject message) {
        return
                with().
                        body(message).
                        given().
                        header(CONTENT_TYPE_HEADER, CRNK_CONTENT_TYPE).
                        when().
                        post(url);
    }
}