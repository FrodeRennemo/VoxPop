package service;

import org.json.*;

import java.util.ArrayList;

/**
 * Created by andreaskalstad on 09/09/15.
 */
public class JSONParser {

    private JSONObject obj;

    public ArrayList parse(String json) throws JSONException {
        ArrayList<Location> array = new ArrayList<Location>();
        JSONArray arr = new JSONArray(json);

        for (int i = 0; i < arr.length(); i++)
        {
            Location location = new Location(arr.getJSONObject(i).getString("address"), arr.getJSONObject(i).getString("name"));
            array.add(location);
        }
        return array;
    }
}
