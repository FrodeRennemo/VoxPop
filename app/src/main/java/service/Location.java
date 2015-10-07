package service;

import android.graphics.Bitmap;

/**
 * Created by andreaskalstad on 10/09/15.
 */
public class Location {
    private String _id;
    private String name;
    private String address;
    private String location;
    private String tlf;
    private String email;
    private String opening_hours;
    private String age_limit;
    private String meta;
    private String picture;

    public Location(String newId, String address, String newLoc, String name, String tlf, String email, String opening_hours, String age_limit, String meta) {
        this._id = newId.trim();
        this.address = address.trim();
        this.location = newLoc.trim();
        this.name = name.trim();
        this.age_limit = age_limit.trim();
        this.email = email.trim();
        this.tlf = tlf.trim();
        this.opening_hours = opening_hours.trim();
        this.meta = meta.toLowerCase().trim();
    }

    public String getId() {
        return _id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {return location;}

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getAge_limit() {
        return age_limit;
    }

    public void setAge_limit(String age_limit) {
        this.age_limit = age_limit;
    }

    public String getMeta(){
        return meta;
    }

    public void setFeatures(String features) {
        this.meta = meta;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture(){return picture; }
}
