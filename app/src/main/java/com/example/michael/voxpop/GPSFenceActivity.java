package com.example.michael.voxpop;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import activitySupport.GPSTracker;

/**
 * Created by andreaskalstad on 25/09/15.
 */
public class GPSFenceActivity extends AppCompatActivity {
    private TextView locationView;
    private Location position;
    private Location locationOppdal;
    Button btnShowLocation;
    GPSTracker gps;
    private TextView myLocationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Share a picture!");
        locationView = (TextView) findViewById(R.id.location);
        myLocationView = (TextView) findViewById(R.id.myLocation);
        position = new Location("Position");
        locationOppdal = new Location("Oppdal");
        locationOppdal.setLatitude(62.59451130000001);
        locationOppdal.setLongitude(9.673516999999947);
        btnShowLocation = (Button) findViewById(R.id.test);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                gps = new GPSTracker(GPSFenceActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){
                    position.setLongitude(gps.getLongitude());
                    position.setLatitude(gps.getLatitude());

                    Location locationOppdal = new Location("Oppdal");
                    locationOppdal.setLatitude(63.4168799);
                    locationOppdal.setLongitude(10.4030469);
                    if(position.distanceTo(locationOppdal) < 15) {
                        Toast.makeText(getApplicationContext(), "Du er innenfor!" + position.distanceTo(locationOppdal), Toast.LENGTH_LONG).show();
                        locationView.setText("Oppdal");
                    } else {
                        Toast.makeText(getApplicationContext(), "Du er utenfor!" + position.distanceTo(locationOppdal), Toast.LENGTH_LONG).show();
                    }
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
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
        return true;
    }
}
