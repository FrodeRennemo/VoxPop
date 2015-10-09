package service;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Michael on 09.10.2015.
 */
public class LocationMarker {
    public Marker marker;
    public Location loc;

    public LocationMarker(Marker _marker, Location _loc){
        marker = _marker;
        loc = _loc;
    }

    public Location getLoc() {
        return loc;
    }

    public Marker getMarker() {
        return marker;
    }
}
