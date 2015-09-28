package service;

import org.json.*;

import java.util.ArrayList;

/**
 * Created by andreaskalstad on 09/09/15.
 */
public class JSONParser {

    private JSONObject obj;

    public ArrayList<Location> parse(String json) throws JSONException {
        ArrayList<Location> array = new ArrayList<Location>();
        JSONArray arr = new JSONArray(json);

        for (int i = 0; i < arr.length(); i++) {
            Location location = new Location(arr.getJSONObject(i).getString("_id"), arr.getJSONObject(i).getString("address"), arr.getJSONObject(i).getString("location"), arr.getJSONObject(i).getString("name"),
                    arr.getJSONObject(i).getString("tlf"), arr.getJSONObject(i).getString("email"), arr.getJSONObject(i).getString("opening_hours"),
                    arr.getJSONObject(i).getString("age_limit"), arr.getJSONObject(i).getString("meta"));
            array.add(location);
        }
        return array;
    }
}
