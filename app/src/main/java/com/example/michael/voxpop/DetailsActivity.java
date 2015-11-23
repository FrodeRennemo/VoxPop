/*
 * Copyright (C) 2013 Manuel Peinado
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.michael.voxpop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.reflect.Type;

import service.DBHandler;
import service.Location;
import service.Model;

public class DetailsActivity extends AppCompatActivity {

    Model model;
    ImageView _img;
    ProgressBar _progress;
    Location loc;
    Menu menu;


    TextView _address;
    TextView _age_text;
    TextView _open_text;
    TextView _contact_text;

    private GoogleMap mMap;
    double latitude;
    double longitude;


    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
        model = new Model(this.getApplicationContext());
        dbHandler = new DBHandler(getApplicationContext());
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        _img = (ImageView) findViewById(R.id.image_header);

        _progress = (ProgressBar) findViewById(R.id.progressBar);

        Intent i = getIntent();
        Type type = new TypeToken<Location>(){}.getType();
        loc = new Gson().fromJson(i.getStringExtra("selected"), type);

        String[] latlong = loc.getLocation().split(",");
        latitude = Double.parseDouble(latlong[0].trim());
        longitude = Double.parseDouble(latlong[1].trim());


        setUpMapIfNeeded();

        if(model.checkFavoriteExists(loc.getId())){
            _img.setImageBitmap(model.getLocationBitmap(loc.getId()));
            _progress.setVisibility(View.GONE);

        }else {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(loc.getProfile_image(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    _img.setImageBitmap(loadedImage);
                    _progress.setVisibility(View.GONE);
                }
            });
        }
        collapsingToolbar.setTitle(loc.getName());

        getSupportActionBar().setTitle(loc.getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _address = (TextView) findViewById(R.id.address_text);
        _age_text = (TextView) findViewById(R.id.age_text);
        _open_text = (TextView) findViewById(R.id.open_text);
        _contact_text = (TextView) findViewById(R.id.contact_text);

        _address.setText(loc.getAddress());
        _age_text.setText(loc.getAge_limit());
        _open_text.setText(loc.getOpening_hours());
        _contact_text.setText(loc.getTlf() + "\n" + loc.getEmail());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu this_menu) {
        getMenuInflater().inflate(R.menu.menu_details, this_menu);
        this.menu = this_menu;
        if(menu != null){
            checkFavorite();
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.take_photo) {
            goToCamera();
            return true;
        }

        if (id == R.id.add_favorite) {
            dbHandler.setFavoritesUpdated(true);
            Model model = new Model(this.getApplicationContext());
            if(_img.getDrawable() != null) {
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) _img.getDrawable());
                loc.setPicture(bitmapDrawable.getBitmap());
            }
            model.addFavorite(loc);
            if(menu != null) {
                checkFavorite();
            }
            return true;
        }
        if(id == R.id.rem_favorite){
            dbHandler.setFavoritesUpdated(true);
            model.deleteFavorite(loc.getId());
            if(menu != null) {
                checkFavorite();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    public void checkFavorite(){
        MenuItem add_item = menu.findItem(R.id.add_favorite);
        MenuItem rem_item = menu.findItem(R.id.rem_favorite);
        if(model.checkFavoriteExists(loc.getId())){
            rem_item.setVisible(true);
            add_item.setVisible(false);
        }else {
            rem_item.setVisible(false);
            add_item.setVisible(true);
        }
    }

    public void goToCamera() {
        Intent i = new Intent(this, CameraActivity.class);
        i.putExtra("nightclubId", loc.getId());
        startActivity(i);
    }
    public void goToMoments(View v) {
        Intent i = new Intent(this, GalleryActivity.class);
        i.putExtra("nightclubId", loc.getId());
        startActivity(i);
    }

    public void takePhoto(View v){
        Toast.makeText(getApplicationContext(), "You are not at "+loc.getName(), Toast.LENGTH_SHORT).show();
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

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LatLng diskon = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(diskon).title(loc.getName()).snippet(loc.getAddress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.frodemarker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(diskon, 13));
        mMap.setMyLocationEnabled(true);
    }
}