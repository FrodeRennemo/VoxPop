package service;

import android.os.AsyncTask;

import com.example.michael.voxpop.MainActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andreaskalstad on 09/09/15.
 */
public class HTTPRequest extends AsyncTask<String, Void, String> {

    private final String USER_AGENT = "Mozilla/5.0";
    private Model model;
    private AsyncListener asyncListener;
    private ArrayList jsonArray;
    private JSONParser jsonParser;

    public void setAsyncListener(AsyncListener asyncListener){
        this.asyncListener = asyncListener;
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuffer response = new StringBuffer();
        try {

            // HTTP GET request
            String url = "\n" + "http://voxpop-app.herokuapp.com/nightclubs";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String res) {
        Model model = new Model();
        jsonArray = new ArrayList();
        jsonParser = new JSONParser();
        try {
            jsonArray = jsonParser.parse(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        asyncListener.asyncDone(jsonArray);
    }
}
