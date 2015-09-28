package service;

/**
 * Created by andreaskalstad on 10/09/15.
 */
public class Location {
    private String _id;
    private String name;
    private String address;
    private String tlf;
    private String email;
    private String opening_hours;
    private String age_limit;
    private String[] features;

    public Location(String newId, String address, String name, String tlf, String email, String opening_hours, String age_limit, String features) {
        this._id = newId;
        this.address = address;
        this.name = name;
        this.age_limit = age_limit;
        this.email = email;
        this.tlf = tlf;
        this.opening_hours = opening_hours;
        this.features = features.split(",");
        for(int i = 0; i<this.features.length; i++){
            this.features[i] = this.features[i].trim();
        }

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

    public String[] getFeatures() {
        return features;
    }

    public void setFeatures(String[] features) {
        this.features = features;
    }
}
