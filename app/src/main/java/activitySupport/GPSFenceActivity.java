package activitySupport;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;

import com.example.michael.voxpop.R;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;

/**
 * Created by andreaskalstad on 25/09/15.
 */
public class GPSFenceActivity extends AppCompatActivity {
    private ArrayList<Geofence> mGeofenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mGeofenceList = new ArrayList<>();
    }

    private void createFence(){
        mGeofenceList.add(new Geofence.Builder()
            // Set the request ID of the geofence. This is a string to identify this
            // geofence.
            .setRequestId("Kalvskinnsgata")

            .setCircularRegion(
                    63.429437, 10.3854547,
                    50
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build());
    }
}
