package com.example.michael.voxpop;

import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import activitySupport.BitmapResizer;
import activitySupport.ImageAdapter;
import activitySupport.ImageCollection;
import service.AmazonS3Listener;
import service.GetImageFromFS;
import service.Model;

/**
 * Created by andreaskalstad on 12/10/15.
 */
public class GalleryActivity extends AppCompatActivity implements AmazonS3Listener{
    private ViewPager viewPager;
    private ImageAdapter adapter;
    private Model model;
    private int position = 0;
    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        model = new Model();
        model.getImageId("5628ceed64e18c1020f122be", "562a842336c16c0b00a44d43", getApplicationContext(), this);
        img= (ImageView) findViewById(R.id.image);
    }

    @Override
    public void asyncDone(final ArrayList<String> idArray) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int height = displaymetrics.heightPixels;
        final int width = displaymetrics.widthPixels;
        final ArrayList<String> id = idArray;
        Picasso.with(getApplicationContext()).load("https://s3-eu-west-1.amazonaws.com/voxpoppic/"+id.get(0)).resize(width, height).noFade().into(img);
        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                position++;
                if (idArray.size() > position) {
                    Picasso.with(getApplicationContext()).load("https://s3-eu-west-1.amazonaws.com/voxpoppic/" + id.get(position)).resize(width, height).into(img);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
