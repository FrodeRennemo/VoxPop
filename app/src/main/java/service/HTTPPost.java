package service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
            String url = "\n" + "http://voxpop-app.herokuapp.com/nightclubs/55e30700f8360ee827846812/upload";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            con.setDoOutput(true);

            ObjectOutputStream out=new ObjectOutputStream(con.getOutputStream());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            Bitmap picture = BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
            picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
            BitmapDataObject bitmapDataObject = new BitmapDataObject();
            bitmapDataObject.serializedBitmap = stream.toByteArray();
            bitmapDataObject.value  = "";//your string value
            out.writeObject(bitmapDataObject);
            out.flush();
            out.close();

            System.out.println(con.getResponseCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return null;
    }
}
