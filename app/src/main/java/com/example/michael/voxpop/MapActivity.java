package com.example.michael.voxpop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import activitySupport.Mood;
import service.AsyncListener;
import service.GetDetails;
import service.Location;
import service.LocationMarker;
import service.Model;

/**
 * Created by andreaskalstad on 07/10/15.
 */
public class MapActivity extends AppCompatActivity implements AsyncListener {
    private GoogleMap mMap;
    private ArrayList<Location> markerLocations;
    private ArrayList<LocationMarker> locationMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        locationMarkers = new ArrayList<>();
        GetDetails req = new GetDetails();
        req.setAsyncListener(this);
        Model model = new Model();
        model.getDetails(req);
    }

    @Override
    public void asyncDone(ArrayList<Location> res) {
        markerLocations =  res;
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

        return super.onOptionsItemSelected(item);
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
        LatLng camPos = new LatLng(0.000000, 0.000000);
        boolean camPosSet = false;
        for(Location l : markerLocations){
            if(!l.getLocation().equals("")){
                String regex_coords = "([+-]?\\d+\\.?\\d+)\\s*,\\s*([+-]?\\d+\\.?\\d+)";
                Pattern compiledPattern2 = Pattern.compile(regex_coords, Pattern.CASE_INSENSITIVE);
                Matcher matcher2 = compiledPattern2.matcher(l.getLocation());
               if(matcher2.matches()){
                   String [] locString = l.getLocation().split(",");
                   LatLng pos = new LatLng(Double.parseDouble(locString[0]), Double.parseDouble(locString[1]));
                   MarkerOptions m = new MarkerOptions().position(pos).title(l.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.frodemarker)).snippet(l.getAddress());
                   Marker marker = mMap.addMarker(m);
                   locationMarkers.add(new LocationMarker(marker, l));
                   if(!camPosSet){
                       String [] camString = l.getLocation().split(",");
                       camPos = new LatLng(Double.parseDouble(camString[0]), Double.parseDouble(camString[1]));
                       camPosSet = true;
                   }
               }
            }
        }

        mMap.setOnInfoWindowClickListener(new Myonclicklistener(this));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camPos, 14));
        mMap.setMyLocationEnabled(true);
    }

    private class Myonclicklistener implements GoogleMap.OnInfoWindowClickListener
    {
        MapActivity mActivity;
        public Myonclicklistener(MapActivity _activity){
            mActivity = _activity;
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent i = new Intent(mActivity, DetailsActivity.class);
            Type type = new TypeToken<Location>(){}.getType();
            Location loc = null;
            for(LocationMarker lm : locationMarkers){
                if(lm.getMarker().getId().equals(marker.getId())){
                    loc = lm.getLoc();
                }
            }
            String json = new Gson().toJson(loc, type);
            i.putExtra("selected", json);
            startActivity(i);
        }
    }

}
