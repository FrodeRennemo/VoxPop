package service;

import android.os.AsyncTask;
import android.os.Environment;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by andreaskalstad on 16/09/15.
 */
public class HerokuImagePost extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            String url = "http://voxpop-app.herokuapp.com/nightclubs";
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(params[0]));
            post.setEntity(new StringEntity(params[1]));
            post.setEntity(new StringEntity(params[2]));

            HttpResponse response = httpclient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println(statusCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
