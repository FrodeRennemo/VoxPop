package service;

import android.content.Context;

/**
 * Created by Andreas on 21.10.2015.
 */
public class ModelHelper {
    String city;
    byte[] data;
    String id;
    String nightclub;
    Context ctx;

    public ModelHelper(byte[] data, String id) {
        this.data = data;
        this.id = id;
    }

    public ModelHelper(String city, String nightclub, Context ctx) {
        this.city = city;
        this.nightclub = nightclub;
        this.ctx = ctx;
    }

    public String getCity() {
        return city;
    }

    public String getNightclub() {
        return nightclub;
    }

    public Context getCtx() {
        return ctx;
    }

    public byte[] getData() {
        return data;
    }

    public String getId() {
        return id;
    }
}
