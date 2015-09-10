package service;

/**
 * Created by andreaskalstad on 10/09/15.
 */
public class Location {
    private String name;
    private String address;

    public Location(String name, String address) {
        this.address = address;
        this.name = name;
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
}
