package service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;

/*import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by andreaskalstad on 16/09/15.
 */
public class PostImageToFS extends AsyncTask<byte[], Void, byte[]> {
   /* private CognitoCachingCredentialsProvider credentialsProvider;
    private Context ctx;

    public PostImageToFS(Context ctx){
        this.ctx = ctx;
        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                ctx,
                "eu-west-1:0942601d-f34e-4196-ac6f-a2e443274140", // Identity Pool ID
                Regions.EU_WEST_1 // Region
        );


        // Initialize the Cognito Sync client
        CognitoSyncManager syncClient = new CognitoSyncManager(
                ctx,
                Regions.EU_WEST_1, // Region
                credentialsProvider);

        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("nightclub", "pictures");
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //Your handler code here
            }
        });
    } */

    @Override
    protected byte[] doInBackground(byte[]... params) {
 /*       try {
            // Create an S3 client
            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

            // Set the region of your S3 bucket
            s3.setRegion(Region.getRegion(Regions.EU_WEST_1));

            TransferUtility transferUtility = new TransferUtility(s3, ctx);

            Bitmap picture = BitmapFactory.decodeByteArray(params[0], 0, params[0].length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            //String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.txt");
            fos.write(imageBytes);
            fos.close();
            File test = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.txt");

            TransferObserver observer = transferUtility.upload(
                    "voxpopupload",     /* The bucket to upload to */
/*                    "test",    /* The key for the uploaded object */
/*                    test        /* The file where the data to upload exists */
/*            );
        } catch (Exception e) {
            System.out.println(e.getMessage());

        } */
        return null;
        }
}
