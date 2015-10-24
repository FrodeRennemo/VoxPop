package com.example.michael.voxpop;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import activitySupport.ImageAdapter;
import activitySupport.ImageCollection;
import service.GetImageFromFS;
import service.Model;

/**
 * Created by andreaskalstad on 12/10/15.
 */
public class GalleryActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ImageAdapter adapter;
    private Model model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery");
        model = new Model();
        model.getImageId("5628ceed64e18c1020f122be", "562a842336c16c0b00a44d43", getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new ImageAdapter(this);

        /*ImageCollection imageCollection = new ImageCollection();
        if (imageCollection.getImageCollection().size() > 0) {
            for (int i = 0; i < 1; i++) {
                adapter.instantiateItem(viewPager, i);
            }
        }
        viewPager.setAdapter(adapter); */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            adapter.startUpdate(viewPager);
            viewPager.setAdapter(adapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
