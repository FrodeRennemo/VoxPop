package service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andreaskalstad on 16/09/15.
 */
public class HTTPPost extends AsyncTask<byte[], Void, byte[]> {
    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected byte[] doInBackground(byte[]... params) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "yourfile");
        try {

            // HTTP GET request
            //String url = "http://voxpop-app.herokuapp.com/nightclubs/55e30700f8360ee827846812/upload";
            String url = "http://voxpop-app.herokuapp.com/nightclubs/56094cf75732010b0034caf9/upload";
            HttpClient httpclient = new DefaultHttpClient();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

    /*        Bitmap picture = BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
            picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
            BitmapDataObject bitmapDataObject = new BitmapDataObject();
            bitmapDataObject.serializedBitmap = stream.toByteArray();
            bitmapDataObject.value  = "file";//your string value
            */

            Bitmap picture = BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.txt");
            fos.write(imageBytes);
            fos.close();
            File test = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.txt");


            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            //builder.addTextBody("image", encodedImage);
            builder.addBinaryBody("file", test, ContentType.TEXT_PLAIN, "file");

            HttpPost post = new HttpPost(url);
            post.setEntity(builder.build());
            HttpResponse response = httpclient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println(statusCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return null;
    }
}
