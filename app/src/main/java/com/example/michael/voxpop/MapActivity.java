package com.example.michael.voxpop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import service.Location;

/**
 * Created by andreaskalstad on 07/10/15.
 */
public class MapActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private ArrayList<Location> markerLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        Type type = new TypeToken<ArrayList<Location>>(){}.getType();
        markerLocations = new Gson().fromJson(i.getStringExtra("locations"), type);

        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        String [] camString = markerLocations.get(0).getLocation().split(",");
        LatLng camPos = new LatLng(Double.parseDouble(camString[0]), Double.parseDouble(camString[1]));
        for(Location l : markerLocations){
            String [] locString = l.getLocation().split(",");
            LatLng pos = new LatLng(Double.parseDouble(locString[0]), Double.parseDouble(locString[1]));
            MarkerOptions m = new MarkerOptions().position(pos).title(l.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.frodemarker));
            mMap.addMarker(m);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camPos, 13));
        mMap.setMyLocationEnabled(true);
    }
}
