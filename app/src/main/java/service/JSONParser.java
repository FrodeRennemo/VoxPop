package service;

import org.json.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import activitySupport.FeedListItem;

/**
 * Created by andreaskalstad on 09/09/15.
 */
public class JSONParser {

    public ArrayList<Location> parse(String json) throws JSONException {
        ArrayList<Location> array = new ArrayList<>();
        JSONArray arr = new JSONArray(json);

        for (int i = 0; i < arr.length(); i++) {
            JSONObject city = arr.getJSONObject(i).getJSONObject("city");
            Location location = new Location(arr.getJSONObject(i).getString("_id"), arr.getJSONObject(i).getString("address"), arr.getJSONObject(i).getString("location"), arr.getJSONObject(i).getString("name"),
                    arr.getJSONObject(i).getString("tlf"), arr.getJSONObject(i).getString("email"), arr.getJSONObject(i).getString("opening_hours"),
                    arr.getJSONObject(i).getString("age_limit"), arr.getJSONObject(i).getString("meta"), city.getString("_id"), city.getString("name"),arr.getJSONObject(i).getString("page_id"));
            array.add(location);
        }
        return array;
    }

    public ArrayList<String> parseId(String json) throws JSONException {
        ArrayList<String> array = new ArrayList<>();
        JSONArray arr = new JSONArray(json);

        for (int i = 0; i < arr.length(); i++) {
            String id = arr.getJSONObject(i).getString("pic_id");
            array.add(id);
        }
        return array;
    }

    public ArrayList<FeedListItem> parseFeed(JSONArray json)  {
        ArrayList<FeedListItem> array = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            try {
                String id = json.getJSONObject(i).getString("message");
                String created = json.getJSONObject(i).getString("created_time");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
                FeedListItem feedListItem = new FeedListItem(id, null, dateFormat.parse(created));
                array.add(feedListItem);
            } catch (JSONException e) {

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return array;
    }
}



