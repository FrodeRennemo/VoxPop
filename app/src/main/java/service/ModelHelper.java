package service;

import android.content.Context;

/**
 * Created by Andreas on 21.10.2015.
 */
public class ModelHelper {
    String city;
    byte[] data;
    String id;
    Context ctx;
    int cameraId;

    public ModelHelper(byte[] data, String id, int cameraId) {
        this.data = data;
        this.id = id;
        this.cameraId = cameraId;
    }

    public ModelHelper(String city, String id, Context ctx) {
        this.id = id;
        this.city = city;
        this.ctx = ctx;
    }

    public String getCity() {
        return city;
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

    public int getCameraId() {
        return cameraId;
    }
}
