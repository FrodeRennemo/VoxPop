package service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import org.jets3t.service.S3Service;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by andreaskalstad on 19/10/15.
 */
public class GetImageFromFS extends AsyncTask<String, Void, Void>{
    private CognitoCachingCredentialsProvider credentialsProvider;
    private Context ctx;
    private static int imagecount = 1;

    public GetImageFromFS(Context ctx){
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
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            // Create an S3 client
            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

            // Set the region of your S3 bucket
            s3.setRegion(Region.getRegion(Regions.EU_WEST_1));

            TransferUtility transferUtility = new TransferUtility(s3, ctx);

            File test = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloadtest.txt");

            TransferObserver observer = transferUtility.download(
                    "voxpoppic",
                    params[0],
                    test
            );

  /*          AWSCredentials awsCredentials = new AWSCredentials(YourAccessKey, YourAwsSecretKey);
            S3Service s3Service = new RestS3Service(awsCredentials);

            S3Object[] objects = s3Service.listObjects("voxpoppic");

            for(int i = 0; i<objects.length; i++) {
                S3Object obj = s3Service.getObject("voxpoppic", ""+i);
                file = obj.getDataInputStream();
            } */
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
