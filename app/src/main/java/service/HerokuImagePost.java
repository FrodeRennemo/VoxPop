package service;

import android.os.AsyncTask;
import android.os.Environment;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by andreaskalstad on 16/09/15.
 */
public class HerokuImagePost extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            String url = "http://voxpop-app.herokuapp.com/cities/"+params[1]+"/nightclubs/"+params[2]+"/pics";
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost post = new HttpPost(url);
            StringEntity se = new StringEntity("pic_id="+params[0]);
            se.setContentType("application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response = httpclient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println(statusCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
