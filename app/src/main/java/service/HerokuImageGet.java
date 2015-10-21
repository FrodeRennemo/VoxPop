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

import java.util.ArrayList;

/**
 * Created by andreaskalstad on 16/09/15.
 */
public class HerokuImageGet extends AsyncTask<ModelHelper, Void, String> {
    private Context ctx;
    private ArrayList<String> jsonArray;
    private JSONParser jsonParser;

    @Override
    protected String doInBackground(ModelHelper... params) {
        HttpResponse response = null;
        ctx = params[0].getCtx();
        try {
            String url = "http://voxpop-app.herokuapp.com/"+params[0].getCity()+"/"+params[0].getNightclub();
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet get = new HttpGet(url);

            response = httpclient.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println(statusCode);
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
