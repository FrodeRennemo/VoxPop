package service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andreaskalstad on 16/09/15.
 */
public class HerokuImageGet extends AsyncTask<ModelHelper, Void, String> {
    private Context ctx;
    private ArrayList<String> jsonArray;
    private JSONParser jsonParser;
    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected String doInBackground(ModelHelper... params) {
        StringBuffer response = new StringBuffer();
        ctx = params[0].getCtx();
        try {
            String url = "http://voxpop-app.herokuapp.com/cities/"+params[0].getCity()+"/nightclubs/"+params[0].getNightclub()+"/pics";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String res){
        Model model = new Model();
        jsonArray = new ArrayList<String>();
        jsonParser = new JSONParser();
        try {
            jsonArray = jsonParser.parseId(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        model.getImage(jsonArray, ctx);
    }
}
