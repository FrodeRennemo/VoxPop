package com.example.michael.voxpop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by andreaskalstad on 07/10/15.
 */
public class MapActivity extends AppCompatActivity {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        LatLng kick = new LatLng(58.1439075,7.9953157);
        LatLng vaktbua = new LatLng(58.1406624,7.9976006);
        LatLng charliesbar = new LatLng(58.1434306, 7.9941797);
        LatLng nylon = new LatLng(58.1481489,8.0030592);
        mMap.addMarker(new MarkerOptions().position(kick).title("Kick"));
        mMap.addMarker(new MarkerOptions().position(vaktbua).title("Vaktbua"));
        mMap.addMarker(new MarkerOptions().position(charliesbar).title("Charlies Bar"));
        mMap.addMarker(new MarkerOptions().position(nylon).title("NYLON Nightclub"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kick, 13));
        mMap.setMyLocationEnabled(true);
    }
}
