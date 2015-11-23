package com.example.michael.voxpop;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import activitySupport.CameraPreview;
import service.Model;
import service.PostImageToFS;

public class CameraActivity extends AppCompatActivity {
    private static Camera mCamera;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static int cameraId;
    private Model model;
    private FrameLayout preview;
    private boolean flash;
    private Camera.Parameters p;
    private boolean pause;
    private ImageView captureButton;
    private ImageView changeButton;
    private ImageView flashButton;
    private ImageView resetButton;
    private ImageView sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        model = new Model();

        // Create an instance of Camera
        cameraId = findBackFacingCamera();
        try {
            getCameraInstance();
        } catch(RuntimeException e){
            Toast.makeText(getApplicationContext(), "Camera not found", Toast.LENGTH_LONG).show();
            Log.d("err", e.getMessage());
            finish();
        }

        p = mCamera.getParameters();
        setCameraView();

        captureButton = (ImageView) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );


        changeButton = (ImageView) findViewById(R.id.camera_change);
        changeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cameraId == 0) {
                            mCamera.release();
                            cameraId = findFrontFacingCamera();
                            getCameraInstance();
                            setCameraView();
                        } else {
                            mCamera.release();
                            cameraId = findBackFacingCamera();
                            getCameraInstance();
                            setCameraView();
                        }
                    }
                }
        );

        flashButton = (ImageView) findViewById(R.id.button_flash);
        flashButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cameraId == 0) {
                            if (flash) {
                                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                mCamera.setParameters(p);
                                flash = false;
                            } else {
                                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                mCamera.setParameters(p);
                                flash = true;
                            }
                        }
                    }
                }
        );
    }

    private void setCameraView(){
        Method rotateMethod;
        try {
            rotateMethod = Camera.class.getMethod("setDisplayOrientation", int.class);
            rotateMethod.invoke(mCamera, 90);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.removeAllViews();
        preview.addView(mPreview);
    }

    private int findFrontFacingCamera(){
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public void getCameraInstance() {
        try {
            releaseCameraAndPreview();
            if (cameraId == 0) {
                mCamera = Camera.open();
            }
            else {
                mCamera = Camera.open();
            }
        } catch (Exception e) {
            Log.e("Error", "failed to open Camera");
            e.printStackTrace();
        }
        /*
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            // Camera is not available (in use or does not exist)
        }
        */
    }

    private void releaseCameraAndPreview() {
        //mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatemen

        return super.onOptionsItemSelected(item);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        public static final String TAG = "";

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            captureButton.setVisibility(View.GONE);
            changeButton.setVisibility(View.GONE);
            flashButton.setVisibility(View.GONE);
            resetButton = (ImageView) findViewById(R.id.button_reset);
            sendButton = (ImageView) findViewById(R.id.button_send);
            resetButton.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
            final byte[] pic = data;
            sendButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        /*File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        if (pictureFile == null) {
                            Log.d(TAG, "Error creating media file, check storage permissions");
                            return;
                        } */
                            PostImageToFS postImageToFS = new PostImageToFS(getApplicationContext());
                            Intent i = getIntent();
                            model.sendImage(postImageToFS, pic, UUID.randomUUID().toString(), i.getStringExtra("nightclubId"), cameraId);
                            finish();
                        }
                    });
            resetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCameraView();
                        captureButton.setVisibility(View.VISIBLE);
                        changeButton.setVisibility(View.VISIBLE);
                        flashButton.setVisibility(View.VISIBLE);
                        resetButton.setVisibility(View.GONE);
                        sendButton.setVisibility(View.GONE);
                    }
                });
        }
    };

    @Override
    protected void onPause(){
        super.onPause();
        mCamera.release();
        pause = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(pause) {
            getCameraInstance();
            setCameraView();
            pause = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.release();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.slide_right_exit, R.anim.slide_left_exit);
        // or call onBackPressed()
        return true;
    }
    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override this method to be able to override the transition
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_right_exit, R.anim.slide_left_exit);
    }
}
