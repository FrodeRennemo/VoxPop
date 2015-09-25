package com.example.michael.voxpop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.reflect.Type;

import service.Location;

public class DetailsActivity extends AppCompatActivity {
    TextView _address;
    TextView _age_text;
    TextView _open_text;
    TextView _contact_text;
    ImageView _img;
    ProgressBar _progress;
    Location loc;
    private GoogleMap mMap;
    double latitude;
    double longitude;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        _img = (ImageView) findViewById(R.id.detail_image);
        _address = (TextView) findViewById(R.id.address_text);
        _age_text = (TextView) findViewById(R.id.age_text);
        _open_text = (TextView) findViewById(R.id.open_text);
        _contact_text = (TextView) findViewById(R.id.contact_text);
        _progress = (ProgressBar) findViewById(R.id.progressBar);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage("http://www.visitnorway.com/ImageVaultFiles/id_14949/cf_13/Nidelven-Sven-Erik-Knoff.JPG", new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                _img.setImageBitmap(loadedImage);
                _progress.setVisibility(View.GONE);
            }
        });

        Intent i = getIntent();
        Type type = new TypeToken<Location>(){}.getType();
        loc = new Gson().fromJson(i.getStringExtra("selected"), type);

        _address.setText(loc.getAddress());
        _age_text.setText(loc.getAge_limit());
        _open_text.setText(loc.getOpening_hours());
        _contact_text.setText(loc.getTlf()+"\n"+loc.getEmail());


        String[] latlong = loc.getAddress().split(",");
        latitude = Double.parseDouble(latlong[0].trim());
        longitude = Double.parseDouble(latlong[1].trim());
        getSupportActionBar().setTitle(loc.getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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

        return super.onOptionsItemSelected(item);
    }

    public void goToCamera() {
        Intent i = new Intent(this, CameraActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        // or call onBackPressed()
        return true;
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
        mMap.addMarker(new MarkerOptions().position(diskon).title(loc.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(diskon, 13));
        mMap.setMyLocationEnabled(true);
    }

}
